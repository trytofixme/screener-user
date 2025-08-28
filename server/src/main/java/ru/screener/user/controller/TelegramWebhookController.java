package ru.screener.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.screener.user.dto.telegram.TelegramUpdate;
import ru.screener.user.helpers.RequestDispatcher;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TelegramWebhookController {

    private final RequestDispatcher requestDispatcher;

    @PostMapping("/telegram/webhook")
    public ResponseEntity<Void> onUpdate(@RequestBody TelegramUpdate telegramUpdate) {
        log.info("Webhook update: {}", telegramUpdate);
        requestDispatcher.enqueue(telegramUpdate);

        return ResponseEntity.ok().build();
    }
}
