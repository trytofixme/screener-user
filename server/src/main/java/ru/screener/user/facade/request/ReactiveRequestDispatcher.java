package ru.screener.user.facade.request;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import ru.screener.user.dto.telegram.webhook.TelegramUpdate;
import ru.screener.user.service.deduplication.DeduplicationService;
import ru.screener.user.utils.telegram.TelegramUpdateUtils;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveRequestDispatcher {

    private static final Duration PER_UPDATE_TIMEOUT = Duration.ofSeconds(7);

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
                .filterWhen(upd -> deduplicationService.firstTime(upd.getUpdateId())
                        .timeout(Duration.ofSeconds(2))
                        .onErrorReturn(false))
                .groupBy(TelegramUpdateUtils::extractChatKey, 1)
                .flatMap(group -> group
                                .concatMap(this::routeMono)
                                .name("chat-" + group.key())
                                .tag("chat", group.key())
                        , 50)
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
        return Mono.defer(() -> requestRouter.route(telegramUpdate))
                .timeout(PER_UPDATE_TIMEOUT)
                .onErrorResume(ex -> {
                    log.warn("Update {} handling failed: {}", telegramUpdate.getUpdateId(), ex.toString(), ex);
                    return Mono.empty();
                });
    }
}
