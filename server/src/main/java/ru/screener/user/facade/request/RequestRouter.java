package ru.screener.user.facade.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
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

    public Mono<Void> route(TelegramUpdate telegramUpdate) {
        return Mono.defer(() -> {
            CallbackQuery callbackQuery = telegramUpdate.getCallbackQuery();

            if (callbackQuery != null) {
                return callbackService.handleCallback(callbackQuery);
            }

            Message message = telegramUpdate.getMessage();
            if (message != null) {
                return messageService.handleMessage(message);
            }

            return Mono.fromRunnable(() -> log.info("Ignored update fields: {}", telegramUpdate)).then();
        });
    }
}
