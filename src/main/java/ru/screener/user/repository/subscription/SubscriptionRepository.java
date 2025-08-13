package ru.screener.user.repository.subscription;

import org.springframework.data.repository.CrudRepository;
import ru.screener.user.entity.subscription.Subscription;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, String> {

    boolean existsByUserId(String telegramId);

    List<Subscription> findAllByEndDateBefore(LocalDateTime endDate);
}
