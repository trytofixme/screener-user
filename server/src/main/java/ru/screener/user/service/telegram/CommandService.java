package ru.screener.user.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.screener.user.model.state.State;
import ru.screener.user.entity.user.TelegramUser;
import ru.screener.user.integrations.telegram.TelegramClient;
import ru.screener.user.dto.telegram.request.SendMessageRequest;
import ru.screener.user.builder.request.send.SendMessageRequestBuilder;
import ru.screener.user.service.user.UserStateService;
import ru.screener.user.builder.keyboard.KeyboardButtonsText;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandService {

    private final SendMessageRequestBuilder requestBuilder;
    private final TelegramClient telegramClient;
    private final UserStateService userStateService;

    public Mono<Void> handleCommand(final TelegramUser user, final String commandText) {
        return Mono.defer(() -> {
                    log.info("Handle command: {}", commandText);
                    SendMessageRequest request = resolveMessageRequest(commandText, user);
                    return telegramClient.sendMessage(request)
                            .doOnSuccess(v -> log.info("Sent command reply to chat {}", user.getChatId()));
                })
                .onErrorResume(e -> {
                    String username = user.getUsername() != null ? user.getUsername() : "unknown";
                    log.error("Failed to handle command for user {}: {}", username, e.toString(), e);
                    return Mono.empty();
                });
    }

    private SendMessageRequest resolveMessageRequest(String commandText, TelegramUser user) {
        return switch (commandText) {
            case KeyboardButtonsText.START_COMMAND -> {
                userStateService.setState(user, State.NONE);
                yield requestBuilder.getStartRequest(user);
            }
            default -> requestBuilder.getDefaultRequest(user);
        };
    }
}
