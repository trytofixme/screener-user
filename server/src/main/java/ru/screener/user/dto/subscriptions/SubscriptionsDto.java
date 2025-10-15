package ru.screener.user.dto.subscriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.screener.user.model.subscription.SubscriptionPlan;
import ru.screener.user.model.subscription.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SubscriptionsDto {

    @JsonProperty(value = "telegram_id")
    private Long telegramId;

    @JsonProperty(value = "plan")
    private SubscriptionPlan plan;

    @JsonProperty(value = "status")
    private SubscriptionStatus status;

    @JsonProperty(value = "started_at")
    private LocalDateTime startedAt;

    @JsonProperty(value = "expires_at")
    private LocalDateTime expiresAt;

    @JsonProperty(value = "canceled_at")
    private LocalDateTime canceledAt;

    @JsonProperty(value = "auto_renew")
    private boolean autoRenew;

    @JsonProperty(value = "price_amount")
    private BigDecimal priceAmount;

    @JsonProperty(value = "price_currency")
    private String priceCurrency;

    @JsonProperty(value = "ext_provider")
    private String externalProvider;

    @JsonProperty(value = "ext_subscription_id")
    private String externalSubscriptionId;
}
