package ru.screener.user.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.screener.user.dto.telegram.webhook.CallbackQuery;
import ru.screener.user.dto.telegram.webhook.From;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackService {

    //TODO: Разобраться с подпиской
    public Mono<Void> handleCallback(CallbackQuery callbackQuery) {
        return Mono.defer(() -> {
                    final From user = callbackQuery.getFrom();
                    if (user == null) {
                        return Mono.<Void>empty();
                    }

                    log.info("Handle callback: chatId={}, textPreview={}",
                            user.getId(), callbackQuery.getMessage());

                    return Mono.<Void>empty();
                })
                .onErrorResume(e -> {
                    try {
                        String username = callbackQuery.getFrom() != null ? callbackQuery.getFrom().getUsername() : "unknown";
                        log.error("Failed to handle update for user {}: {}", username, e.toString(), e);
                    } catch (Exception ignore) { }
                    return Mono.<Void>empty();
                });
    }
}
