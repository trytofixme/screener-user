package ru.screener.user.builder.request.send;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.builder.keyboard.KeyboardBuilder;
import ru.screener.user.dto.telegram.request.SendMessageRequest;
import ru.screener.user.entity.settings.RsiSettings;
import ru.screener.user.entity.user.TelegramUser;
import ru.screener.user.service.settings.RsiSettingsService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageRequestBuilder {

    private final RsiSettingsService rsiSettingsService;
    private final KeyboardBuilder keyboardBuilder;

    public SendMessageRequest getStartRequest(TelegramUser user) {
        boolean haveSubscription = true;

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.START_MESSAGE)
                .setReplyMarkup(haveSubscription ? keyboardBuilder.mainKeyboard() : keyboardBuilder.defaultKeyboard());
    }

    public SendMessageRequest getMainMenuRequest(TelegramUser user) {
        boolean haveSubscription = true;

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
            
            🔴 Шорт: RSI ≥ %s за %s;
            🟢 Лонг: RSI ≤ %s за %s.
            """,
            SendMessageRequestText.RSI_HELP_MESSAGE,
            SendMessageRequestText.CURRENT_SETTINGS_MESSAGE,
            settings.getShortRsi(), settings.getShortTimeFrame().getTranslation(),
            settings.getLongRsi(), settings.getLongTimeFrame().getTranslation()
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
                
                🔴 Шорт: RSI ≥ %s за %s;
                🟢 Лонг: RSI ≤ %s за %s.
                """,
                SendMessageRequestText.AFTER_UPDATE_SETTINGS_MESSAGE,
                settings.getShortRsi(), settings.getShortTimeFrame().getTranslation(),
                settings.getLongRsi(), settings.getLongTimeFrame().getTranslation()
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
//
//    private void sendAccess(AppUser u) {
//        String text = """
//        ✅ Выбери тариф и оплати удобной криптовалютой (USDT, USDC, BTC, ETH).
//        Доступ откроется автоматически в течение 1 минуты.""";
//        telegramClient.sendMessage(new SendMessageRequest(u.getChatId(), text, Keyboards.accessInline(), PARSE, true));
//    }
//


    public SendMessageRequest getDefaultRequest(TelegramUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(SendMessageRequestText.UNKNOWN_COMMAND_MESSAGE);
    }
}
