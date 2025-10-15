package ru.screener.user.entity.subscription;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.screener.user.entity.user.TelegramUser;
import ru.screener.user.model.subscription.SubscriptionPlan;
import ru.screener.user.model.subscription.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscriptions",
        indexes = {
                @Index(name = "ix_subscriptions_user_id", columnList = "user_id"),
                @Index(name = "ix_subscriptions_status", columnList = "status"),
                @Index(name = "ix_subscriptions_user_status", columnList = "user_id,status")
        })
public class Subscription {

    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private TelegramUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private SubscriptionPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private SubscriptionStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "auto_renew", nullable = false)
    private boolean autoRenew;

    @Column(name = "price_amount", precision = 18, scale = 2)
    private BigDecimal priceAmount;

    @Column(name = "price_currency", length = 16)
    private String priceCurrency; // ex: "USD","EUR","RUB"

    @Column(name = "ext_provider", length = 32)
    private String externalProvider; // ex: "yookassa"

    @Column(name = "ext_subscription_id", length = 128)
    private String externalSubscriptionId;

    @Version
    private Integer version;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
