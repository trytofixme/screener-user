package ru.screener.user.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.screener.user.dto.telegram.Chat;
import ru.screener.user.dto.telegram.From;
import ru.screener.user.dto.telegram.Message;
import ru.screener.user.entity.settings.TimeFrame;
import ru.screener.user.entity.state.State;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.integrations.telegram.TelegramClient;
import ru.screener.user.integrations.telegram.request.SendMessageRequest;
import ru.screener.user.service.settings.RsiSettingsService;
import ru.screener.user.service.telegram.request.RequestBuilder;
import ru.screener.user.service.user.UserService;
import ru.screener.user.service.user.UserStateService;
import ru.screener.user.utils.keyboard.KeyboardButtons;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private static final String TIME_FRAME_STRING = "TIME_FRAME";
    private static final String RSI_STRING = "RSI";

    private final UserService userService;
    private final UserStateService userStateService;
    private final RsiSettingsService rsiSettingsService;
    private final RequestBuilder requestBuilder;
    private final TelegramClient telegramClient;
    private final CommandService commandService;

    public Mono<Void> handleMessage(Message message) {
        return Mono.defer(() -> {
                    final String messageText = message.getText();
                    if (messageText == null) {
                        return Mono.empty();
                    }

                    log.info("Handle message: chatId={}, textPreview={}",
                            message.getChat() != null ? message.getChat().getId() : "unknown", messageText);

                    // upsert user + read state
                    return Mono.fromCallable(() -> {
                                AppUser user = upsertUser(message);
                                State state = userStateService.getState(user);
                                return new UserCtx(user, state, messageText);
                            })
                            .subscribeOn(Schedulers.boundedElastic())

                            // Команда — short-circuit
                            .flatMap(ctx -> {
                                if (ctx.messageText().startsWith("/")) {
                                    return commandService.handleCommand(ctx.user(), ctx.messageText());
                                }
                                return Mono.just(ctx);
                            })

                            // Если уже ответили командой — тут прилетит Void, пропускаем дальше
                            .flatMap(obj -> {
                                if (obj instanceof UserCtx ctx) {
                                    // Есть ожидаемый ввод — применяем
                                    if (ctx.state() != State.NONE) {
                                        return tryApplyNumericInput(ctx.user(), ctx.state(), ctx.messageText());
                                    }
                                    // Обычная клавиатурная команда/текст
                                    SendMessageRequest request = resolveMessageRequest(ctx.messageText(), ctx.user());
                                    return telegramClient.sendMessage(request)
                                            .doOnSuccess(v -> log.info("Sent message to chat {}", ctx.user().getChatId()));
                                }
                                return Mono.empty();
                            });
                })
                .onErrorResume(e -> {
                    try {
                        String username = message.getFrom() != null ? message.getFrom().getUsername() : "unknown";
                        log.error("Failed to handle update for user {}: {}", username, e.toString(), e);
                    } catch (Exception ignore) { }
                    return Mono.empty();
                });
    }

    private AppUser upsertUser(Message message) {
        From from = Objects.requireNonNull(message.getFrom(), "from is null");
        Chat chat = Objects.requireNonNull(message.getChat(), "chat is null");
        return userService.upsertFromTelegram(
                from.getId(), chat.getId(),
                from.getUsername(), from.getFirstName(), from.getLastName()
        );
    }

    private SendMessageRequest resolveMessageRequest(String messageText, AppUser user) {
        return switch (messageText) {
            case KeyboardButtons.BACK_TO_MENU_COMMAND -> requestBuilder.getMainMenuRequest(user);
            case KeyboardButtons.SETTINGS_COMMAND -> requestBuilder.getSettingsRequest(user);
            case KeyboardButtons.PROFILE_COMMAND -> requestBuilder.getProfileRequest(user);
            case KeyboardButtons.SHORT_TIME_FRAME_COMMAND -> {
                userStateService.setState(user, State.WAIT_TIME_FRAME_SHORT);
                yield requestBuilder.getTimeFramesRequest(user);
            }
            case KeyboardButtons.LONG_TIME_FRAME_COMMAND -> {
                userStateService.setState(user, State.WAIT_TIME_FRAME_LONG);
                yield requestBuilder.getTimeFramesRequest(user);
            }
            case KeyboardButtons.SHORT_RSI_VALUE_COMMAND -> {
                userStateService.setState(user, State.WAIT_RSI_SHORT);
                yield requestBuilder.getRsiValueRequest(user);
            }
            case KeyboardButtons.LONG_RSI_VALUE_COMMAND -> {
                userStateService.setState(user, State.WAIT_RSI_LONG);
                yield requestBuilder.getRsiValueRequest(user);
            }
            default -> requestBuilder.getDefaultRequest(user);
        };
    }

    private Mono<Void> tryApplyNumericInput(AppUser user, State state, String messageText) {
        return Mono.fromCallable(() -> {
                    if (state.name().contains(TIME_FRAME_STRING)) {
                        final TimeFrame tf = TimeFrame.fromString(messageText);
                        if (tf == null) throw new IllegalArgumentException("Неверный таймфрейм");
                        switch (state) {
                            case WAIT_TIME_FRAME_SHORT -> rsiSettingsService.updateTimeFrames(user, tf, null);
                            case WAIT_TIME_FRAME_LONG -> rsiSettingsService.updateTimeFrames(user, null, tf);
                        }
                    } else if (state.name().contains(RSI_STRING)) {
                        final BigDecimal rsi = new BigDecimal(messageText);
                        if (rsi.scale() > 2) throw new IllegalArgumentException("Слишком много знаков после запятой");
                        if (rsi.compareTo(BigDecimal.ZERO) < 0 || rsi.compareTo(new BigDecimal("100")) > 0) {
                            throw new IllegalArgumentException("RSI должен быть в диапазоне 0..100");
                        }
                        switch (state) {
                            case WAIT_RSI_SHORT -> rsiSettingsService.updateShortRsi(user, rsi);
                            case WAIT_RSI_LONG -> rsiSettingsService.updateLongRsi(user, rsi);
                        }
                    } else {
                        // неизвестный state — просто игнор или лог
                        log.warn("Unexpected numeric-input state: {}", state);
                    }
                    userStateService.setState(user, State.NONE);
                    return requestBuilder.getSettingsUpdatedRequest(user);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(telegramClient::sendMessage)
                .onErrorResume(ex -> {
                    log.warn("Bad numeric input '{}': {}", messageText, ex.getMessage());
                    return Mono.fromCallable(() -> requestBuilder.getErrorRequest(user))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(telegramClient::sendMessage)
                            .then();
                })
                .then();
    }

    private record UserCtx(AppUser user, State state, String messageText) {}
}
