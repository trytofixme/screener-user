package ru.screener.user.facade.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.dto.telegram.CallbackQuery;
import ru.screener.user.dto.telegram.Message;
import ru.screener.user.dto.telegram.TelegramUpdate;
import ru.screener.user.service.telegram.CallbackService;
import ru.screener.user.service.telegram.MessageService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestRouter {

    private final MessageService messageService;
    private final CallbackService callbackService;

    public void route(TelegramUpdate telegramUpdate) {
        CallbackQuery callbackQuery = telegramUpdate.getCallbackQuery();

        if (callbackQuery != null) {
            callbackService.handleCallback(callbackQuery);
            return;
        }

        Message message = telegramUpdate.getMessage();
        if (message != null) {
            messageService.handleMessage(message);
            return;
        }

        log.info("Ignored update fields: {}", telegramUpdate);
    }
}
