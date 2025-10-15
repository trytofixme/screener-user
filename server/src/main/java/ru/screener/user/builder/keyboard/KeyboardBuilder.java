package ru.screener.user.builder.keyboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.dto.telegram.keyboard.InlineKeyboardDto;
import ru.screener.user.dto.telegram.keyboard.KeyboardDto;

import java.util.List;

import static ru.screener.user.builder.keyboard.KeyboardButtonsText.*;
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
                                        .setText(SHORT_DUMP_VALUE_COMMAND),
                                new KeyboardDto.KeyboardButton()
                                        .setText(LONG_DUMP_VALUE_COMMAND)
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

    public InlineKeyboardDto accessInlineKeyboard() {
        return new InlineKeyboardDto()
                .setKeyboard(List.of(
                        List.of(
                                new InlineKeyboardDto.KeyboardButton()
                                        .setText(ACCESS_1_MONTH)
                                        .setUrl(PAY_URL)
                        ),
                        List.of(
                                new InlineKeyboardDto.KeyboardButton()
                                        .setText(ACCESS_6_MONTHS)
                                        .setUrl(PAY_URL)
                        ),
                        List.of(
                                new InlineKeyboardDto.KeyboardButton()
                                        .setText(ACCESS_1_YEAR)
                                        .setUrl(PAY_URL)
                        )
                ));
    }
}
