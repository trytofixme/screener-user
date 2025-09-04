package ru.screener.user.facade.request;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import ru.screener.user.dto.telegram.TelegramUpdate;
import ru.screener.user.service.deduplication.DeduplicationService;
import ru.screener.user.utils.telegram.TelegramUpdateUtils;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveRequestDispatcher {

    private final RequestRouter requestRouter;
    private final DeduplicationService deduplicationService;

    private final Sinks.Many<TelegramUpdate> sink = Sinks.many()
            .unicast()
            .onBackpressureBuffer();

    private Disposable subscription;

    @PostConstruct
    public void init() {
        this.subscription = sink.asFlux()
                .filter(Objects::nonNull)
                .filterWhen(telegramUpdate -> deduplicationService.firstTime(telegramUpdate.getUpdateId()))
                .groupBy(TelegramUpdateUtils::extractChatKey)
                .flatMap(group -> group
                        .concatMap(this::routeMono)
                        .onErrorContinue((ex, val) -> {
                            var update = (TelegramUpdate) val;
                            log.error("Update handling failed", ex);
                        })
                )
                .subscribe(
                        ok -> {},
                        ex -> log.error("Dispatcher stream failed", ex),
                        () -> log.warn("Dispatcher stream completed")
                );
    }

    @PreDestroy
    public void shutdown() {
        if (subscription != null) {
            subscription.dispose();
        }
    }

    public Mono<Void> enqueue(TelegramUpdate telegramUpdate) {
        Sinks.EmitResult emitResult = sink.tryEmitNext(telegramUpdate);
        if (emitResult.isFailure()) {
            log.warn("Sink backpressure: {}", emitResult);
        }

        return Mono.empty();
    }

    private Mono<Void> routeMono(TelegramUpdate telegramUpdate) {
        return Mono.fromRunnable(() -> requestRouter.route(telegramUpdate))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
