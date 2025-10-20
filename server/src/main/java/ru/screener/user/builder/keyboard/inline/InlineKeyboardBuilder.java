package ru.screener.user.builder.keyboard.inline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.dto.telegram.keyboard.InlineKeyboardDto;
import ru.screener.user.entity.user.TelegramUser;

import java.util.List;

import static ru.screener.user.builder.keyboard.inline.InlineKeyboardButtonsText.ACCESS_1_MONTH;
import static ru.screener.user.builder.keyboard.inline.InlineKeyboardButtonsText.ACCESS_1_YEAR;
import static ru.screener.user.builder.keyboard.inline.InlineKeyboardButtonsText.ACCESS_6_MONTHS;

@Slf4j
@Service
@RequiredArgsConstructor
public class InlineKeyboardBuilder {

    public InlineKeyboardDto accessInlineKeyboard(TelegramUser user) {
        return new InlineKeyboardDto()
                .setKeyboard(List.of(
                        List.of(
                                new InlineKeyboardDto.KeyboardButton()
                                        .setText(ACCESS_1_MONTH)
                                        .setCallbackData(user.getId().toString())
                        ),
                        List.of(
                                new InlineKeyboardDto.KeyboardButton()
                                        .setText(ACCESS_6_MONTHS)
                                        .setCallbackData(user.getId().toString())
                        ),
                        List.of(
                                new InlineKeyboardDto.KeyboardButton()
                                        .setText(ACCESS_1_YEAR)
                                        .setCallbackData(user.getId().toString())
                        )
                ));
    }
}
