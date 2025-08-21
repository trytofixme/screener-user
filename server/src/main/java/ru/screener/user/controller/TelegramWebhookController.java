package ru.screener.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.screener.user.service.telegram.TelegramUpdateService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/telegram")
@RequiredArgsConstructor
public class TelegramWebhookController {

    private final TelegramUpdateService updateService;

    @PostMapping("/webhook")
    public void onUpdate(@RequestBody Map<String, Object> update) {
        log.debug("Webhook update: {}", update.keySet());
        updateService.handleUpdate(update);
    }
}
