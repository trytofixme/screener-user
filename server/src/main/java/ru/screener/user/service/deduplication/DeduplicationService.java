package ru.screener.user.service.deduplication;

public interface DeduplicationService {

    boolean firstTime(Long updateId);
}
