package ru.screener.user.service.deduplication;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class CaffeineDeduplicationServiceTest {

    private final CaffeineDeduplicationService service = new CaffeineDeduplicationService();

    @Test
    void firstTimeReturnsTrueWhenIdIsNull() {
        StepVerifier.create(service.firstTime(null))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void firstTimeReturnsTrueThenFalseForSameId() {
        Long id = 1L;
        StepVerifier.create(service.firstTime(id))
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(service.firstTime(id))
                .expectNext(false)
                .verifyComplete();
    }
}
