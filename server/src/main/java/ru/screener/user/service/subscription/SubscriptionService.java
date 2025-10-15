package ru.screener.user.service.subscription;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.entity.subscription.Subscription;
import ru.screener.user.entity.user.TelegramUser;
import ru.screener.user.mapper.subscriptions.SubscriptionsMapper;
import ru.screener.user.model.subscription.SubscriptionPlan;
import ru.screener.user.model.subscription.SubscriptionStatus;
import ru.screener.user.repository.subscription.SubscriptionRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionsMapper subscriptionsMapper;
    private final SubscriptionRepository repository;

    @Transactional
    public Subscription getOrCreateSubscription(TelegramUser user) {
        return repository.findByUserIdOrderByStartedAtDesc(user.getId()).orElseGet(() -> createDefaultSubscription(user));
    }

    public boolean CheckForActiveNotFreeSubscription(Subscription subscription) {
        return (subscription.getStatus() == SubscriptionStatus.ACTIVE) && (subscription.getPlan() != SubscriptionPlan.FREE);
    }
    private Subscription createDefaultSubscription(TelegramUser user) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        // Default values
        subscription.setPlan(SubscriptionPlan.FREE);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartedAt(LocalDateTime.now(ZoneOffset.UTC));
        subscription.setExpiresAt(LocalDateTime.now(ZoneOffset.UTC).plusYears(1));

        return repository.save(subscription);
    }
}
