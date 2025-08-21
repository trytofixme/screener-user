package ru.screener.user.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.entity.AppUser;
import ru.screener.user.entity.UserState;
import ru.screener.user.integrations.telegram.TelegramClient;
import ru.screener.user.model.dto.AnswerCallbackQueryRequest;
import ru.screener.user.model.dto.EditMessageTextRequest;
import ru.screener.user.model.dto.SendMessageRequest;
import ru.screener.user.service.user.UserService;
import ru.screener.user.ui.Keyboards;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@SuppressWarnings("unchecked")
@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramUpdateService {

    private static final String PARSE = "Markdown";

    private final UserService userService;
    private final TelegramClient telegramClient;

    public void handleUpdate(Map<String, Object> update) {
        Map<String, Object> message = (Map<String, Object>) update.get("message");
        Map<String, Object> callback = (Map<String, Object>) update.get("callback_query");

        if (message != null) {
            handleMessage(message);
        } else if (callback != null) {
            handleCallback(callback);
        } else {
            log.info("Игнорируем апдейт: {}", update.keySet());
        }
    }

    private void handleMessage(Map<String, Object> msg) {
        Map<String, Object> from = (Map<String, Object>) msg.get("from");
        Map<String, Object> chat = (Map<String, Object>) msg.get("chat");
        String text = (String) msg.get("text");

        AppUser user = userService.upsertFromTelegram(
                ((Number) from.get("id")).longValue(),
                ((Number) chat.get("id")).longValue(),
                (String) from.get("username"),
                (String) from.get("first_name"),
                (String) from.get("last_name")
        );

        // если ждем числа — перехватываем и сохраняем
        var state = userService.getState(user);
        if (state != UserState.State.NONE && text != null) {
            applyNumericInput(user, state, text);
            userService.setState(user, UserState.State.NONE);
            sendSettings(user, "✅ Готово. Текущие настройки:");
            return;
        }

        if (text == null) return;
        switch (text) {
            case "/start" -> sendMainMenu(user);
            case "📊 Биржи" -> sendExchanges(user);
            case "🛠 Настройки" -> sendSettings(user, settingsHelp());
            case "💳 Доступ" -> sendAccess(user);
            case "👤 Профиль" -> sendProfile(user);
            default -> sendDefaultMessage(user);
        }
    }

    private void handleCallback(Map<String, Object> cb) {
        Map<String, Object> from = (Map<String, Object>) cb.get("from");
        Map<String, Object> msg = (Map<String, Object>) cb.get("message");
        String data = (String) cb.get("data");

        AppUser user = userService.upsertFromTelegram(
                ((Number) from.get("id")).longValue(),
                ((Number)((Map<String,Object>)msg.get("chat")).get("id")).longValue(),
                (String) from.get("username"),
                (String) from.get("first_name"),
                (String) from.get("last_name")
        );

        Integer messageId = ((Number) msg.get("message_id")).intValue();

        switch (data) {
            case "EXCH_TOGGLE_BYBIT" -> {
                var e = userService.toggleBybit(user);
                telegramClient.editMessage(new EditMessageTextRequest(
                        user.getChatId(), messageId,
                        exchangesText(), Keyboards.exchangesInline(e.isBybitEnabled(), e.isMoexEnabled()), PARSE));
            }
            case "EXCH_TOGGLE_MOEX" -> {
                var e = userService.toggleMoex(user);
                telegramClient.editMessage(new EditMessageTextRequest(
                        user.getChatId(), messageId,
                        exchangesText(), Keyboards.exchangesInline(e.isBybitEnabled(), e.isMoexEnabled()), PARSE));
            }
            case "SET_PERIOD_LONG"  -> userService.setState(user, UserState.State.WAIT_PERIOD_LONG);
            case "SET_PERIOD_SHORT" -> userService.setState(user, UserState.State.WAIT_PERIOD_SHORT);
            case "SET_PERIOD_DUMP"  -> userService.setState(user, UserState.State.WAIT_PERIOD_DUMP);
            case "SET_PERCENT_LONG" -> userService.setState(user, UserState.State.WAIT_PERCENT_LONG);
            case "SET_PERCENT_SHORT"-> userService.setState(user, UserState.State.WAIT_PERCENT_SHORT);
            case "SET_PERCENT_DUMP" -> userService.setState(user, UserState.State.WAIT_PERCENT_DUMP);
            case "BACK_MAIN" -> sendMainMenu(user);
            default -> {}
        }
        telegramClient.answerCallback(new AnswerCallbackQueryRequest(
                (String) cb.get("id"), null, false));
        if (data.startsWith("SET_")) {
            telegramClient.sendMessage(new SendMessageRequest(
                    user.getChatId(),
                    "Введи числовое значение (только число).",
                    null, PARSE, true));
        }
    }

    private void applyNumericInput(AppUser user, UserState.State state, String text) {
        try {
            if (state.name().contains("PERCENT")) {
                var val = new BigDecimal(text.replace(",", "."));
                switch (state) {
                    case WAIT_PERCENT_LONG -> userService.updatePercents(user, val, null, null);
                    case WAIT_PERCENT_SHORT -> userService.updatePercents(user, null, val, null);
                    case WAIT_PERCENT_DUMP -> userService.updatePercents(user, null, null, val);
                }
            } else {
                int v = Integer.parseInt(text);
                switch (state) {
                    case WAIT_PERIOD_LONG -> userService.updatePeriods(user, v, null, null);
                    case WAIT_PERIOD_SHORT -> userService.updatePeriods(user, null, v, null);
                    case WAIT_PERIOD_DUMP -> userService.updatePeriods(user, null, null, v);
                }
            }
        } catch (Exception ex) {
            telegramClient.sendMessage(new SendMessageRequest(user.getChatId(), "❗ Неверный формат. Попробуй снова.", null, PARSE, true));
            log.warn("Bad numeric input '{}': {}", text, ex.getMessage());
        }
    }

    private void sendDefaultMessage(AppUser user) {
        telegramClient.sendMessage(new SendMessageRequest(
                user.getChatId(),
                "Прости, я тебя не понимаю",
                Collections.emptyMap(), PARSE, true));
    }

    private void sendMainMenu(AppUser u) {
        telegramClient.sendMessage(new SendMessageRequest(
                u.getChatId(),
                "Главное меню:",
                Keyboards.mainReplyKeyboard(), PARSE, true));
    }

    private String exchangesText() {
        return """
        *Включить / выключить сигналы с бирж:*
        Если стоит ✅ — биржа ВКЛЮЧЕНА""";
    }

    private void sendExchanges(AppUser u) {
        var e = userService.getExchanges(u);
        telegramClient.sendMessage(new SendMessageRequest(
                u.getChatId(), exchangesText(), Keyboards.exchangesInline(e.isBybitEnabled(), e.isMoexEnabled()), PARSE, true));
    }

    private String settingsHelp() {
        return """
    🟢 *Лонг*: пришлём сигнал, если монета выросла на X% за N минут
    🔴 *Шорт*: пришлём сигнал, если монета выросла на X% за N минут (индикатор шорта)
    🔻 *Дамп*: пришлём сигнал, если монета упала на X% за N минут

    ⚙️ *Текущие настройки:*""";
    }

    private void sendSettings(AppUser u, String header) {
        var s = userService.getSettings(u);
        String text = String.format("""
        %s
        🟢 Лонг: %s%% за %d мин.
        🔴 Шорт: %s%% за %d мин.
        🔻 Дамп: %s%% за %d мин.""",
                header,
                s.getLongPercent(), s.getLongPeriodMin(),
                s.getShortPercent(), s.getShortPeriodMin(),
                s.getDumpPercent(), s.getDumpPeriodMin()
        );
        telegramClient.sendMessage(new SendMessageRequest(u.getChatId(), text, Keyboards.settingsInline(), PARSE, true));
    }

    private void sendAccess(AppUser u) {
        String text = """
        ✅ Выбери тариф и оплати удобной криптовалютой (USDT, USDC, BTC, ETH).
        Доступ откроется автоматически в течение 1 минуты.""";
        telegramClient.sendMessage(new SendMessageRequest(u.getChatId(), text, Keyboards.accessInline(), PARSE, true));
    }

    private void sendProfile(AppUser u) {
        String until = u.getSubscriptionUntil() == null ? "нет активной подписки" : "до " + u.getSubscriptionUntil();
        String text = String.format("""
        Профиль пользователя %s
        Доступ: %s""", u.getUsername(), until);
        telegramClient.sendMessage(new SendMessageRequest(u.getChatId(), text, null, PARSE, true));
    }
}
