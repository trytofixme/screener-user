package ru.screener.user.helpers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.entity.settings.RsiSettings;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.integrations.telegram.request.SendMessageRequest;
import ru.screener.user.service.settings.RsiSettingsService;
import ru.screener.user.ui.Keyboards;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestBuilder {

    private final RsiSettingsService rsiSettingsService;

    public SendMessageRequest getStartRequest(AppUser user) {
        boolean haveSubscription = true;

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(RequestText.START_MESSAGE)
                .setReplyMarkup(haveSubscription ? Keyboards.mainKeyboard() : Keyboards.defaultKeyboard());
    }

    public SendMessageRequest getMainMenuRequest(AppUser user) {
        boolean haveSubscription = true;

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(RequestText.MAIN_MENU_MESSAGE)
                .setReplyMarkup(haveSubscription ? Keyboards.mainKeyboard() : Keyboards.defaultKeyboard());
    }

    public SendMessageRequest getSettingsRequest(AppUser user) {
        RsiSettings settings = rsiSettingsService.getSettingsByUser(user);
        String messageText = String.format(
            """
            %s
            %s
            
            🔴 Шорт: RSI > %d за %s;
            🟢 Лонг: RSI < %d за %s.
            """,
            RequestText.RSI_HELP_MESSAGE,
            RequestText.CURRENT_SETTINGS_MESSAGE,
            settings.getShortRsi(), settings.getShortTimeFrame().getTranslation(),
            settings.getLongRsi(), settings.getLongTimeFrame().getTranslation()
        );

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(messageText)
                .setReplyMarkup(Keyboards.settingsKeyboard());
    }

    public SendMessageRequest getSettingsUpdatedRequest(AppUser user) {
        RsiSettings settings = rsiSettingsService.getSettingsByUser(user);
        String messageText = String.format(
                """
                %s
                
                🔴 Шорт: RSI > %d за %s;
                🟢 Лонг: RSI < %d за %s.
                """,
                RequestText.AFTER_UPDATE_SETTINGS_MESSAGE,
                settings.getShortRsi(), settings.getShortTimeFrame().getTranslation(),
                settings.getLongRsi(), settings.getLongTimeFrame().getTranslation()
        );

        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(messageText)
                .setReplyMarkup(Keyboards.settingsKeyboard());
    }

    public SendMessageRequest getTimeFramesRequest(AppUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(RequestText.TIME_FRAME_COMMAND_MESSAGE)
                .setReplyMarkup(Keyboards.timeFramesKeyboard());
    }

    public SendMessageRequest getRsiValueRequest(AppUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(RequestText.RSI_VALUE_COMMAND_MESSAGE);
    }

    public SendMessageRequest getErrorRequest(AppUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(RequestText.FORMAT_ERROR_MESSAGE);
    }

    public SendMessageRequest getProfileRequest(AppUser user) {
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


    public SendMessageRequest getDefaultRequest(AppUser user) {
        return new SendMessageRequest()
                .setChatId(user.getChatId())
                .setText(RequestText.UNKNOWN_COMMAND_MESSAGE);
    }
}
