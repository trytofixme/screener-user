package ru.screener.user.service.deduplication;

import reactor.core.publisher.Mono;

public interface DeduplicationService {

    Mono<Boolean> firstTime(Long updateId);
}
