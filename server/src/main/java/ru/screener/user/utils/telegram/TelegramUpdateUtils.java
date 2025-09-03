package ru.screener.user.utils.telegram;

import lombok.experimental.UtilityClass;
import ru.screener.user.dto.telegram.Message;
import ru.screener.user.dto.telegram.TelegramUpdate;

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
}

