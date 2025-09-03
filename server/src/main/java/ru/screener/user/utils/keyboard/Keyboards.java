package ru.screener.user.utils.keyboard;

import lombok.experimental.UtilityClass;
import ru.screener.user.dto.keyboard.Keyboard;

import java.util.List;

import static ru.screener.user.entity.settings.TimeFrame.FIFTEEN_MINUTES;
import static ru.screener.user.entity.settings.TimeFrame.FOUR_HOURS;
import static ru.screener.user.entity.settings.TimeFrame.ONE_DAY;
import static ru.screener.user.entity.settings.TimeFrame.ONE_HOUR;
import static ru.screener.user.utils.keyboard.KeyboardButtons.ACCESS_COMMAND;
import static ru.screener.user.utils.keyboard.KeyboardButtons.BACK_TO_MENU_COMMAND;
import static ru.screener.user.utils.keyboard.KeyboardButtons.LONG_RSI_VALUE_COMMAND;
import static ru.screener.user.utils.keyboard.KeyboardButtons.LONG_TIME_FRAME_COMMAND;
import static ru.screener.user.utils.keyboard.KeyboardButtons.PROFILE_COMMAND;
import static ru.screener.user.utils.keyboard.KeyboardButtons.SETTINGS_COMMAND;
import static ru.screener.user.utils.keyboard.KeyboardButtons.SHORT_RSI_VALUE_COMMAND;
import static ru.screener.user.utils.keyboard.KeyboardButtons.SHORT_TIME_FRAME_COMMAND;

@UtilityClass
public final class Keyboards {

    public static Keyboard defaultKeyboard() {
        return new Keyboard()
                .setKeyboard(List.of(List.of(
                        new Keyboard.KeyboardButton()
                                .setText(ACCESS_COMMAND),
                        new Keyboard.KeyboardButton()
                                .setText(PROFILE_COMMAND)
                )))
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false);
    }

    public static Keyboard mainKeyboard() {
        return new Keyboard()
                .setKeyboard(List.of(
                        List.of(
                                new Keyboard.KeyboardButton()
                                        .setText(SETTINGS_COMMAND)
                        ),
                        List.of(
                                new Keyboard.KeyboardButton()
                                        .setText(ACCESS_COMMAND),
                                new Keyboard.KeyboardButton()
                                        .setText(PROFILE_COMMAND)
                )))
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false);
    }

    public static Keyboard settingsKeyboard() {
        return new Keyboard()
                .setKeyboard(List.of(
                        List.of(
                                new Keyboard.KeyboardButton()
                                        .setText(SHORT_TIME_FRAME_COMMAND),
                                new Keyboard.KeyboardButton()
                                        .setText(LONG_TIME_FRAME_COMMAND)
                        ),
                        List.of(
                                new Keyboard.KeyboardButton()
                                        .setText(SHORT_RSI_VALUE_COMMAND),
                                new Keyboard.KeyboardButton()
                                        .setText(LONG_RSI_VALUE_COMMAND)
                        ),
                        List.of(
                                new Keyboard.KeyboardButton()
                                        .setText(BACK_TO_MENU_COMMAND)
                        )))
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);
    }

    public static Keyboard timeFramesKeyboard() {
        return new Keyboard()
                .setKeyboard(List.of(
                        List.of(
                                new Keyboard.KeyboardButton()
                                        .setText(FIFTEEN_MINUTES.getTranslation()),
                                new Keyboard.KeyboardButton()
                                        .setText(ONE_HOUR.getTranslation())
                        ),
                        List.of(
                                new Keyboard.KeyboardButton()
                                        .setText(FOUR_HOURS.getTranslation()),
                                new Keyboard.KeyboardButton()
                                        .setText(ONE_DAY.getTranslation())
                        ),
                        List.of(
                                new Keyboard.KeyboardButton()
                                        .setText(BACK_TO_MENU_COMMAND)
                        )))
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);
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
