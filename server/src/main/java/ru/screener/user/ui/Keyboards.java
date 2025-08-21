package ru.screener.user.ui;

import java.util.List;
import java.util.Map;

public final class Keyboards {
    private Keyboards(){}

    public static Map<String, Object> mainReplyKeyboard() {
        return Map.of(
                "keyboard", List.of(
                        List.of(Map.of("text","📊 Биржи"), Map.of("text","🛠 Настройки")),
                        List.of(Map.of("text","💳 Доступ"), Map.of("text","👤 Профиль"))
                ),
                "resize_keyboard", true,
                "one_time_keyboard", false
        );
    }

    public static Map<String, Object> exchangesInline(boolean bybit, boolean moex) {
        return Map.of("inline_keyboard", List.of(
                List.of(Map.of("text", (bybit ? "✅ " : "⬜ ") + "Bybit", "callback_data", "EXCH_TOGGLE_BYBIT")),
                List.of(Map.of("text", (moex ? "✅ " : "⬜ ") + "МосБиржа", "callback_data", "EXCH_TOGGLE_MOEX")),
                List.of(Map.of("text","⬅ Назад","callback_data","BACK_MAIN"))
        ));
    }

    public static Map<String, Object> settingsInline() {
        return Map.of("inline_keyboard", List.of(
                List.of(Map.of("text","🟢 Период лонг","callback_data","SET_PERIOD_LONG"),
                        Map.of("text","🟢 % лонг","callback_data","SET_PERCENT_LONG")),
                List.of(Map.of("text","🔴 Период шорт","callback_data","SET_PERIOD_SHORT"),
                        Map.of("text","🔴 % шорт","callback_data","SET_PERCENT_SHORT")),
                List.of(Map.of("text","🔻 Период дамп","callback_data","SET_PERIOD_DUMP"),
                        Map.of("text","🔻 % дамп","callback_data","SET_PERCENT_DUMP")),
                List.of(Map.of("text","⬅ Назад","callback_data","BACK_MAIN"))
        ));
    }

    public static Map<String, Object> accessInline() {
        return Map.of("inline_keyboard", List.of(
                List.of(Map.of("text","📄 Доступ на 1 месяц 100$","url","https://pay.example/1m")),
                List.of(Map.of("text","📄 Доступ на 6 месяцев 200$","url","https://pay.example/6m")),
                List.of(Map.of("text","📄 Доступ на 12 месяцев 290$","url","https://pay.example/12m")),
                List.of(Map.of("text","⬅ Назад","callback_data","BACK_MAIN"))
        ));
    }
}
