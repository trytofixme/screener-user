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
                    log.info("Handled message {}", message);
                    if (messageText == null) {
                        return Mono.empty();
                    }

                    return Mono.fromCallable(() -> upsertUser(message))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(user ->
                                    Mono.fromCallable(() -> userStateService.getState(user))
                                            .subscribeOn(Schedulers.boundedElastic())
                                            .flatMap(state -> {
                                                if (messageText.startsWith("/")) {
                                                    commandService.handleCommand(user, messageText);
                                                }

                                                if (state != State.NONE) {
                                                    return tryApplyNumericInput(user, state, messageText);
                                                }

                                                SendMessageRequest request = resolveMessageRequest(messageText, user);
                                                return telegramClient.sendMessage(request)
                                                        .doOnSuccess(v -> log.info("Sent message to chat {}", user.getChatId()));
                                            })
                            );
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
                from.getId(),
                chat.getId(),
                from.getUsername(),
                from.getFirstName(),
                from.getLastName()
        );
    }

    private SendMessageRequest resolveMessageRequest(String messageText, AppUser user) {
        switch (messageText) {
            case KeyboardButtons.BACK_TO_MENU_COMMAND -> {
                return requestBuilder.getMainMenuRequest(user);
            }
            case KeyboardButtons.SETTINGS_COMMAND -> {
                return requestBuilder.getSettingsRequest(user);
            }
//            case ACCESS_COMMAND -> messageBuilder.getAccessRequest(user);
            case KeyboardButtons.PROFILE_COMMAND -> {
                return requestBuilder.getProfileRequest(user);
            }
            case KeyboardButtons.SHORT_TIME_FRAME_COMMAND -> {
                userStateService.setState(user, State.WAIT_TIME_FRAME_SHORT);
                return requestBuilder.getTimeFramesRequest(user);
            }
            case KeyboardButtons.LONG_TIME_FRAME_COMMAND -> {
                userStateService.setState(user, State.WAIT_TIME_FRAME_LONG);
                return requestBuilder.getTimeFramesRequest(user);
            }
            case KeyboardButtons.SHORT_RSI_VALUE_COMMAND -> {
                userStateService.setState(user, State.WAIT_RSI_SHORT);
                return requestBuilder.getRsiValueRequest(user);
            }
            case KeyboardButtons.LONG_RSI_VALUE_COMMAND -> {
                userStateService.setState(user, State.WAIT_RSI_LONG);
                return requestBuilder.getRsiValueRequest(user);
            }
            default -> {
                return requestBuilder.getDefaultRequest(user);
            }
        }
    }

    private Mono<Void> tryApplyNumericInput(AppUser user, State state, String messageText) {
        return Mono.fromRunnable(() -> {
                    if (state.name().contains(TIME_FRAME_STRING)) {
                        final TimeFrame timeFrame = TimeFrame.fromString(messageText);
                        switch (state) {
                            case WAIT_TIME_FRAME_SHORT -> rsiSettingsService.updateTimeFrames(user, timeFrame, null);
                            case WAIT_TIME_FRAME_LONG -> rsiSettingsService.updateTimeFrames(user, null, timeFrame);
                            default -> { /* no-op */ }
                        }
                    }
                    if (state.name().contains(RSI_STRING)) {
                        final BigDecimal rsiValue = new BigDecimal(messageText);
                        switch (state) {
                            case WAIT_RSI_SHORT -> rsiSettingsService.updateShortRsi(user, rsiValue);
                            case WAIT_RSI_LONG -> rsiSettingsService.updateLongRsi(user, rsiValue);
                            default -> { /* no-op */ }
                        }
                    }
                    userStateService.setState(user, State.NONE);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then(Mono.fromCallable(() -> requestBuilder.getSettingsUpdatedRequest(user))
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(telegramClient::sendMessage))
                .onErrorResume(ex -> {
                    log.warn("Bad numeric input '{}': {}", messageText, ex.getMessage());
                    return Mono.fromCallable(() -> requestBuilder.getErrorRequest(user))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(telegramClient::sendMessage);
                });
    }
}
