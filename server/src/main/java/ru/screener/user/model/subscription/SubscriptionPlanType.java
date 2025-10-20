package ru.screener.user.model.subscription;

import lombok.Getter;

@Getter
public enum SubscriptionPlanType {
    FREE("Бесплатная"),
    PREMIUM("Премиум"),
    SUPER("Супер");

    private final String translation;

    SubscriptionPlanType(String translation) {
        this.translation = translation;
    }

    public static String translate(SubscriptionPlanType subscriptionPlan) {
        return subscriptionPlan.translation;
    }
}
