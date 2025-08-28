package ru.screener.user.helpers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.dto.telegram.TelegramUpdate;
import ru.screener.user.service.deduplication.DeduplicationService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestDispatcher {

    private final RequestRouter requestRouter;
    private final DeduplicationService deduplicationService;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public void enqueue(TelegramUpdate telegramUpdate) {
        executorService.submit(() -> {
            final Long updateId = telegramUpdate.getUpdateId();
            if (!deduplicationService.firstTime(updateId)) {
                log.debug("Duplicate update: {}", updateId);
                return;
            }

            try {
                requestRouter.route(telegramUpdate);
            } catch (Exception e) {
                log.error("Update handling failed id={}", updateId, e);
            }
        });
    }
}
