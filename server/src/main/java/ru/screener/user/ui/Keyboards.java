package ru.screener.user.ui;

import ru.screener.user.entity.settings.TimeFrame;

import java.util.List;
import java.util.Map;

public final class Keyboards {

    private Keyboards(){}

    public static Map<String, Object> defaultKeyboard() {
        return Map.of(
                "keyboard", List.of(
                        List.of(Map.of("text", KeyboardButtons.ACCESS_COMMAND), Map.of("text", KeyboardButtons.PROFILE_COMMAND))
                ),
                "resize_keyboard", true,
                "one_time_keyboard", false
        );
    }

    public static Map<String, Object> mainKeyboard() {
        return Map.of(
                "keyboard", List.of(
                        List.of(Map.of("text", KeyboardButtons.SETTINGS_COMMAND)),
                        List.of(Map.of("text", KeyboardButtons.ACCESS_COMMAND), Map.of("text",KeyboardButtons.PROFILE_COMMAND))
                ),
                "resize_keyboard", true,
                "one_time_keyboard", false
        );
    }

    public static Map<String, Object> settingsKeyboard() {
        return Map.of("keyboard", List.of(
                List.of(Map.of("text", KeyboardButtons.SHORT_TIME_FRAME_COMMAND),
                        Map.of("text", KeyboardButtons.LONG_TIME_FRAME_COMMAND)),
                List.of(Map.of("text", KeyboardButtons.SHORT_RSI_VALUE_COMMAND),
                        Map.of("text", KeyboardButtons.LONG_RSI_VALUE_COMMAND)),
                List.of(Map.of("text", KeyboardButtons.BACK_TO_MENU_COMMAND))
                ),
                "resize_keyboard", true,
                "one_time_keyboard", true
        );
    }

    public static Map<String, Object> timeFramesKeyboard() {
        return Map.of("keyboard", List.of(
                        List.of(Map.of("text", TimeFrame.FIFTEEN_MINUTES.getTranslation()),
                                Map.of("text", TimeFrame.ONE_HOUR.getTranslation())),
                        List.of(Map.of("text", TimeFrame.FOUR_HOURS.getTranslation()),
                                Map.of("text", TimeFrame.ONE_DAY.getTranslation())),
                        List.of(Map.of("text", KeyboardButtons.BACK_TO_MENU_COMMAND))
                ),
                "resize_keyboard", true,
                "one_time_keyboard", true
        );
    }

//    public static Map<String, Object> accessInline() {
//        return Map.of("inline_keyboard", List.of(
//                List.of(Map.of("text","📄 Доступ на 1 месяц 100$","url","https://pay.example/1m")),
//                List.of(Map.of("text","📄 Доступ на 6 месяцев 200$","url","https://pay.example/6m")),
//                List.of(Map.of("text","📄 Доступ на 12 месяцев 290$","url","https://pay.example/12m")),
//                List.of(Map.of("text","⬅ Назад","callback_data","BACK_MAIN"))
//        ));
//    }
}
