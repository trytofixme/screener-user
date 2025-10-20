package ru.screener.user.service.subscription;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.entity.subscription.Subscription;
import ru.screener.user.entity.user.TelegramUser;
import ru.screener.user.repository.subscription.SubscriptionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repository;

    @Transactional
    public Subscription tryGetSubscription(TelegramUser user) {
        return repository.findByUserIdOrderByStartedAtDesc(user.getId())
                .orElse(null);
    }
}
