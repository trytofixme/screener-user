package ru.screener.user.repository.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.screener.user.entity.subscription.Subscription;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findByUserIdOrderByStartedAtDesc(UUID userId);
}



