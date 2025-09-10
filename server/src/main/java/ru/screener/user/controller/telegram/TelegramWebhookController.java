package ru.screener.user.controller.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.screener.user.dto.telegram.TelegramUpdate;
import ru.screener.user.facade.request.ReactiveRequestDispatcher;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TelegramWebhookController {

    private final ReactiveRequestDispatcher reactiveDispatcher;

    @PostMapping("/telegram/webhook")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> onUpdate(@RequestBody TelegramUpdate telegramUpdate) {
        log.info("Webhook update!: {}", telegramUpdate);

        return reactiveDispatcher.enqueue(telegramUpdate);
    }
}
