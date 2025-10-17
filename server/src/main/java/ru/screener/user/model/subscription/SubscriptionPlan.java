package ru.screener.user.model.subscription;

import lombok.Getter;

@Getter
public enum SubscriptionPlan {
    FREE("Бесплатная"),
    PREMIUM("Премиум"),
    SUPER("Супер");

    private final String translation;

    SubscriptionPlan(String translation) {
        this.translation = translation;
    }

    public static String translate(SubscriptionPlan subscriptionPlan) {
        return subscriptionPlan.translation;
    }
}
