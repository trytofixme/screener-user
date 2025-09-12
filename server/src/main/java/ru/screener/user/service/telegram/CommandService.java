package ru.screener.user.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.screener.user.entity.state.State;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.integrations.telegram.TelegramClient;
import ru.screener.user.integrations.telegram.request.SendMessageRequest;
import ru.screener.user.service.telegram.request.RequestBuilder;
import ru.screener.user.service.user.UserStateService;
import ru.screener.user.utils.keyboard.KeyboardButtons;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandService {

    private final RequestBuilder requestBuilder;
    private final TelegramClient telegramClient;
    private final UserStateService userStateService;

    public void handleCommand(final AppUser user, final String commandText) {
        Mono.defer(() -> {
                    log.info("Handled command {}", commandText);

                    SendMessageRequest request = resolveMessageRequest(commandText, user);
                    return telegramClient.sendMessage(request)
                            .doOnSuccess(v -> log.info("Sent message to chat {}", user.getChatId()));
                })
                .onErrorResume(e -> {
                    try {
                        String username = user.getUsername() != null ? user.getUsername() : "unknown";
                        log.error("Failed to handle update for user {}: {}", username, e.toString(), e);
                    } catch (Exception ignore) {
                    }
                    return Mono.empty();
                });
    }

    private SendMessageRequest resolveMessageRequest(String messageText, AppUser user) {
        switch (messageText) {
            case KeyboardButtons.START_COMMAND -> {
                userStateService.setState(user, State.NONE);
                return requestBuilder.getStartRequest(user);
            }
            default -> {
                return requestBuilder.getDefaultRequest(user);
            }
        }
    }
}
