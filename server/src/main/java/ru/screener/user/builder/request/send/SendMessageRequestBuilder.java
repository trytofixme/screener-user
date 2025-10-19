package ru.screener.user.builder.request.send;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.builder.keyboard.KeyboardBuilder;
import ru.screener.user.builder.keyboard.inline.InlineKeyboardBuilder;
import ru.screener.user.dto.telegram.request.SendMessageRequest;
import ru.screener.user.entity.settings.RsiSettings;
import ru.screener.user.entity.user.TelegramUser;
import ru.screener.user.service.settings.RsiSettingsService;
import ru.screener.user.service.subscription.SubscriptionService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageRequestBuilder {

    private final RsiSettingsService rsiSettingsService;
    private final KeyboardBuilder keyboardBuilder;
    private final InlineKeyboardBuilder inlineKeyboardBuilder;
    private final SubscriptionService subscriptionService;

    public SendMessageRequest getStartRequest(TelegramUser user) {
        boolean haveSubscription = subscriptionService.tryGetSubscription(user) != null;

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.START_MESSAGE)
                .setReplyMarkup(haveSubscription ? keyboardBuilder.mainKeyboard() : keyboardBuilder.defaultKeyboard());
    }

    public SendMessageRequest getMainMenuRequest(TelegramUser user) {
        boolean haveSubscription = subscriptionService.tryGetSubscription(user) != null;

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.MAIN_MENU_MESSAGE)
                .setReplyMarkup(haveSubscription ? keyboardBuilder.mainKeyboard() : keyboardBuilder.defaultKeyboard());
    }

    public SendMessageRequest getSettingsRequest(TelegramUser user) {
        RsiSettings settings = rsiSettingsService.getOrCreateSettings(user);
        String messageText = String.format(
                """
                %s
                %s
                %s
                
                🔴 *Шорт: RSI ≥ %s за %s*;
                🟢 *Лонг: RSI ≤ %s за %s*.
                
                🔴 *Дамп шорт: при изменении RSI на %s*;
                🟢 *Дамп лонг: при изменении RSI на %s*.
                """,
                SendMessageRequestText.RSI_HELP_MESSAGE,
                SendMessageRequestText.DUMP_HELP_MESSAGE,
                SendMessageRequestText.CURRENT_SETTINGS_MESSAGE,
                settings.getShortRsi(), settings.getShortTimeFrame().getTranslation(),
                settings.getLongRsi(), settings.getLongTimeFrame().getTranslation(),
                settings.getShortDump(), settings.getLongDump()
        );

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(messageText)
                .setReplyMarkup(keyboardBuilder.settingsKeyboard());
    }

    public SendMessageRequest getSettingsUpdatedRequest(TelegramUser user) {
        RsiSettings settings = rsiSettingsService.getOrCreateSettings(user);
        String messageText = String.format(
                """
                %s
                
                🔴 *Шорт: RSI ≥ %s за %s*;
                🟢 *Лонг: RSI ≤ %s за %s*.
                
                🔴 *Дамп шорт: при изменении RSI на %s*;
                🟢 *Дамп лонг: при изменении RSI на %s*.
                """,
                SendMessageRequestText.AFTER_UPDATE_SETTINGS_MESSAGE,
                settings.getShortRsi(), settings.getShortTimeFrame().getTranslation(),
                settings.getLongRsi(), settings.getLongTimeFrame().getTranslation(),
                settings.getShortDump(), settings.getLongDump()
        );

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(messageText)
                .setReplyMarkup(keyboardBuilder.settingsKeyboard());
    }

    public SendMessageRequest getTimeFramesRequest(TelegramUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.TIME_FRAME_COMMAND_MESSAGE)
                .setReplyMarkup(keyboardBuilder.timeFramesKeyboard());
    }

    public SendMessageRequest getRsiValueRequest(TelegramUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.RSI_VALUE_COMMAND_MESSAGE);
    }

    public SendMessageRequest getDumpValueRequest(TelegramUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.DUMP_VALUE_COMMAND_MESSAGE);
    }

    public SendMessageRequest getErrorRequest(TelegramUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.FORMAT_ERROR_MESSAGE);
    }

    public SendMessageRequest getProfileRequest(TelegramUser user) {
        String until = "до 01.01.2030";
        String messageText = String.format(
                """
                Профиль пользователя: %s
                Доступ: %s
                """, user.getUsername(), until);

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(messageText)
                .setParseMode(null);
    }

    public SendMessageRequest getAccessRequest(TelegramUser user) {
        boolean haveSubscription = subscriptionService.tryGetSubscription(user) != null;

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .setChatId(user.getChatId());
        if (!haveSubscription) {
            return sendMessageRequest
                    .setText(SendMessageRequestText.GET_ACCESS_MESSAGE)
                    .setReplyMarkup(inlineKeyboardBuilder.accessInlineKeyboard(user));
        }
        return sendMessageRequest
                .setText(SendMessageRequestText.ACCESS_PERMITTED)
                .setReplyMarkup(keyboardBuilder.mainKeyboard());
    }

    public SendMessageRequest getDefaultRequest(TelegramUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.UNKNOWN_COMMAND_MESSAGE);
    }
}