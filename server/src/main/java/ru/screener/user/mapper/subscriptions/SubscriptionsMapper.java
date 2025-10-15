package ru.screener.user.mapper.subscriptions;

import org.springframework.stereotype.Component;
import ru.screener.user.dto.subscriptions.SubscriptionsDto;
import ru.screener.user.entity.subscription.Subscription;

@Component
public class SubscriptionsMapper {

    public SubscriptionsDto toDto(Subscription subscription) {
        return new SubscriptionsDto()
                .setTelegramId(subscription.getUser().getTgUserId())
                .setPlan(subscription.getPlan())
                .setStatus(subscription.getStatus())
                .setStartedAt(subscription.getStartedAt())
                .setExpiresAt(subscription.getExpiresAt())
                .setCanceledAt(subscription.getCanceledAt())
                .setAutoRenew(subscription.isAutoRenew())
                .setPriceAmount(subscription.getPriceAmount())
                .setPriceCurrency(subscription.getPriceCurrency())
                .setExternalProvider(subscription.getExternalProvider())
                .setExternalSubscriptionId(subscription.getExternalSubscriptionId());
    }
}
