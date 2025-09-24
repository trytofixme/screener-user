package ru.screener.user.builder.keyboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.dto.telegram.keyboard.KeyboardDto;

import java.util.List;

import static ru.screener.user.builder.keyboard.KeyboardButtonsText.ACCESS_COMMAND;
import static ru.screener.user.builder.keyboard.KeyboardButtonsText.BACK_TO_MENU_COMMAND;
import static ru.screener.user.builder.keyboard.KeyboardButtonsText.LONG_RSI_VALUE_COMMAND;
import static ru.screener.user.builder.keyboard.KeyboardButtonsText.LONG_TIME_FRAME_COMMAND;
import static ru.screener.user.builder.keyboard.KeyboardButtonsText.PROFILE_COMMAND;
import static ru.screener.user.builder.keyboard.KeyboardButtonsText.SETTINGS_COMMAND;
import static ru.screener.user.builder.keyboard.KeyboardButtonsText.SHORT_RSI_VALUE_COMMAND;
import static ru.screener.user.builder.keyboard.KeyboardButtonsText.SHORT_TIME_FRAME_COMMAND;
import static ru.screener.user.model.settings.TimeFrame.FIFTEEN_MINUTES;
import static ru.screener.user.model.settings.TimeFrame.FOUR_HOURS;
import static ru.screener.user.model.settings.TimeFrame.ONE_DAY;
import static ru.screener.user.model.settings.TimeFrame.ONE_HOUR;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyboardBuilder {

    public KeyboardDto defaultKeyboard() {
        return new KeyboardDto()
                .setKeyboard(List.of(List.of(
                        new KeyboardDto.KeyboardButton()
                                .setText(ACCESS_COMMAND),
                        new KeyboardDto.KeyboardButton()
                                .setText(PROFILE_COMMAND)
                )))
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false);
    }

    public KeyboardDto mainKeyboard() {
        return new KeyboardDto()
                .setKeyboard(List.of(
                        List.of(
                                new KeyboardDto.KeyboardButton()
                                        .setText(SETTINGS_COMMAND)
                        ),
                        List.of(
                                new KeyboardDto.KeyboardButton()
                                        .setText(ACCESS_COMMAND),
                                new KeyboardDto.KeyboardButton()
                                        .setText(PROFILE_COMMAND)
                )))
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false);
    }

    public KeyboardDto settingsKeyboard() {
        return new KeyboardDto()
                .setKeyboard(List.of(
                        List.of(
                                new KeyboardDto.KeyboardButton()
                                        .setText(SHORT_TIME_FRAME_COMMAND),
                                new KeyboardDto.KeyboardButton()
                                        .setText(LONG_TIME_FRAME_COMMAND)
                        ),
                        List.of(
                                new KeyboardDto.KeyboardButton()
                                        .setText(SHORT_RSI_VALUE_COMMAND),
                                new KeyboardDto.KeyboardButton()
                                        .setText(LONG_RSI_VALUE_COMMAND)
                        ),
                        List.of(
                                new KeyboardDto.KeyboardButton()
                                        .setText(BACK_TO_MENU_COMMAND)
                        )))
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);
    }

    public KeyboardDto timeFramesKeyboard() {
        return new KeyboardDto()
                .setKeyboard(List.of(
                        List.of(
                                new KeyboardDto.KeyboardButton()
                                        .setText(FIFTEEN_MINUTES.getTranslation()),
                                new KeyboardDto.KeyboardButton()
                                        .setText(ONE_HOUR.getTranslation())
                        ),
                        List.of(
                                new KeyboardDto.KeyboardButton()
                                        .setText(FOUR_HOURS.getTranslation()),
                                new KeyboardDto.KeyboardButton()
                                        .setText(ONE_DAY.getTranslation())
                        ),
                        List.of(
                                new KeyboardDto.KeyboardButton()
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
