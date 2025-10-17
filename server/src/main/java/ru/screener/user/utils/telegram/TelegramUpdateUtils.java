package ru.screener.user.utils.telegram;

import lombok.experimental.UtilityClass;
import ru.screener.user.builder.keyboard.KeyboardButtonsText;
import ru.screener.user.dto.telegram.webhook.Message;
import ru.screener.user.dto.telegram.webhook.TelegramUpdate;
import ru.screener.user.model.state.State;
import ru.screener.user.model.subscription.SubscriptionPlan;
import ru.screener.user.model.subscription.SubscriptionStatus;

import java.time.format.DateTimeFormatter;

@UtilityClass
public final class TelegramUpdateUtils {

    public static Long extractChatId(TelegramUpdate telegramUpdate) {
        if (telegramUpdate == null) {
            return null;
        }

        Message message = telegramUpdate.getMessage();
        if (message != null && message.getChat() != null) {
            return message.getChat().getId();
        }

        if (telegramUpdate.getCallbackQuery() != null
                && telegramUpdate.getCallbackQuery().getMessage() != null
                && telegramUpdate.getCallbackQuery().getMessage().getChat() != null) {
            return telegramUpdate.getCallbackQuery().getMessage().getChat().getId();
        }

        return null;
    }

    // Невалидные апдейты отправим в общий “nochat”
    public static String extractChatKey(TelegramUpdate upd) {
        Long chatId = extractChatId(upd);
        return (chatId == null) ? "nochat" : "chat:" + chatId;
    }

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    //TODO: Чекнуть SubscriptionPlan.translate
    public static String TranslateSubscription(SubscriptionPlan plan)
    {
        return switch (plan) {
            case SubscriptionPlan.FREE -> "Бесплатная";
            case SubscriptionPlan.PREMIUM -> "Премиум";
            case SubscriptionPlan.SUPER -> "Супер";
            default -> "Подписка не оформлена";
        };
    }

    public static String TranslateSubscription(SubscriptionStatus status)
    {
        return switch (status) {
            case SubscriptionStatus.ACTIVE -> "Активна";
            case SubscriptionStatus.PENDING -> "Ожидает подтверждения";
            case SubscriptionStatus.CANCELED -> "Отменена";
            case SubscriptionStatus.EXPIRED -> "Истекла";
            default -> "Подписка не оформлена";
        };
    }
}

