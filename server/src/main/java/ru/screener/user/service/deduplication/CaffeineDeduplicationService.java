package ru.screener.user.service.deduplication;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class CaffeineDeduplicationService implements DeduplicationService {

    private final Cache<Long, Boolean> messagesCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(10))
            .maximumSize(100_000)
            .build();

    @Override
    public boolean firstTime(Long updateId) {
        if (updateId == null) {
            return true;
        }

        return messagesCache.asMap().putIfAbsent(updateId, Boolean.TRUE) == null;
    }
}
