package ru.screener.user.exception.subscription;

import jakarta.persistence.EntityNotFoundException;

public class SubscriptionNotFoundException extends EntityNotFoundException {

    public SubscriptionNotFoundException(String telegramId) {
        super(String.format("Subscription for telegramId %s not found", telegramId));
    }
}
