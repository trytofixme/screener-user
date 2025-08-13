package ru.screener.user.service.subscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.entity.subscription.Subscription;
import ru.screener.user.exception.subscription.SubscriptionNotFoundException;
import ru.screener.user.repository.subscription.SubscriptionRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription getSubscriptionByTelegramId(String telegramId) {
        return subscriptionRepository.findById(telegramId)
                .orElseThrow(() -> {
                    log.error("Subscription for telegramId {} not found", telegramId);
                    return new SubscriptionNotFoundException(telegramId);
                });
    }
}
