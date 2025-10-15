package ru.screener.user.builder.request.send;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.builder.keyboard.KeyboardBuilder;
import ru.screener.user.dto.telegram.request.SendMessageRequest;
import ru.screener.user.entity.settings.RsiSettings;
import ru.screener.user.entity.subscription.Subscription;
import ru.screener.user.entity.user.TelegramUser;
import ru.screener.user.model.subscription.SubscriptionStatus;
import ru.screener.user.service.settings.RsiSettingsService;
import ru.screener.user.service.subscription.SubscriptionService;

import static ru.screener.user.utils.telegram.TelegramUpdateUtils.formatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageRequestBuilder {

    private final RsiSettingsService rsiSettingsService;
    private final KeyboardBuilder keyboardBuilder;
    private final SubscriptionService subscriptionService;

    public SendMessageRequest getStartRequest(TelegramUser user) {
        Subscription subscription = subscriptionService.getOrCreateSubscription(user);
        boolean haveSubscription =  subscriptionService.CheckForActiveNotFreeSubscription(subscription);

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.START_MESSAGE)
                .setKeyboard(haveSubscription ?
                        keyboardBuilder.mainKeyboard() : keyboardBuilder.defaultKeyboard());
    }

    public SendMessageRequest getAccessRequest(TelegramUser user) {
        Subscription subscription = subscriptionService.getOrCreateSubscription(user);
        boolean haveSubscription =  subscriptionService.CheckForActiveNotFreeSubscription(subscription);

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .setChatId(user.getChatId());
        if (!haveSubscription) {
            return sendMessageRequest
                    .setText(SendMessageRequestText.GET_ACCESS_MESSAGE)
                    .setInlineKeyboard(keyboardBuilder.accessInlineKeyboard());
        }
        return sendMessageRequest
                .setText(SendMessageRequestText.ACCESS_PERMITTED)
                .setKeyboard(keyboardBuilder.mainKeyboard());
    }

    public SendMessageRequest getMainMenuRequest(TelegramUser user) {
        Subscription subscription = subscriptionService.getOrCreateSubscription(user);
        boolean haveSubscription =  subscriptionService.CheckForActiveNotFreeSubscription(subscription);

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.MAIN_MENU_MESSAGE)
                .setKeyboard(haveSubscription ? keyboardBuilder.mainKeyboard() : keyboardBuilder.defaultKeyboard());
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
                .setKeyboard(keyboardBuilder.settingsKeyboard());
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
                .setKeyboard(keyboardBuilder.settingsKeyboard());
    }

    public SendMessageRequest getTimeFramesRequest(TelegramUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.TIME_FRAME_COMMAND_MESSAGE)
                .setKeyboard(keyboardBuilder.timeFramesKeyboard());
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
        Subscription subscription = subscriptionService.getOrCreateSubscription(user);

        String messageText = String.format(
        """
        Профиль пользователя: %s
        Тип подписки: %s
        Статус: %s
        Доступ: до %s
        """,
                user.getUsername(),
                subscription.getPlan(),
                subscription.getStatus(),
                subscription.getExpiresAt().format(formatter));

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(messageText)
                .setParseMode(null);
    }

    public SendMessageRequest getDefaultRequest(TelegramUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.UNKNOWN_COMMAND_MESSAGE);
    }
}
