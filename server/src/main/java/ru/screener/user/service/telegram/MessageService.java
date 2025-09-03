package ru.screener.user.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.dto.telegram.Chat;
import ru.screener.user.dto.telegram.From;
import ru.screener.user.dto.telegram.Message;
import ru.screener.user.entity.settings.TimeFrame;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.entity.state.State;
import ru.screener.user.integrations.telegram.TelegramClient;
import ru.screener.user.integrations.telegram.request.SendMessageRequest;
import ru.screener.user.service.settings.RsiSettingsService;
import ru.screener.user.service.telegram.request.RequestBuilder;
import ru.screener.user.service.telegram.request.RequestText;
import ru.screener.user.service.user.UserService;
import ru.screener.user.service.user.UserStateService;
import ru.screener.user.utils.keyboard.KeyboardButtons;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private static final String TIME_FRAME_STRING = "TIME_FRAME";
    private static final String RSI_STRING = "RSI";

    private final UserService userService;
    private final UserStateService userStateService;
    private final RsiSettingsService rsiSettingsService;
    private final RequestBuilder requestBuilder;
    private final TelegramClient telegramClient;

    public void handleMessage(Message message) {
        final From from = message.getFrom();
        final Chat chat = message.getChat();
        final String messageText = message.getText();

        if (messageText == null) {
            return;
        }

        final AppUser user = userService.upsertFromTelegram(
                from.getId(),
                chat.getId(),
                from.getUsername(),
                from.getFirstName(),
                from.getLastName()
        );
        final State state = userStateService.getState(user);

        final SendMessageRequest request = resolveMessageRequest(messageText, user);

        // Если статус не пустой, значит предыдущая команда была на изменение настроек
        if (state != State.NONE) {
            tryApplyNumericInput(user, state, messageText);
            return;
        }

        telegramClient.sendMessage(request);
    }

    private SendMessageRequest resolveMessageRequest(String messageText, AppUser user) {
        switch (messageText) {
            case KeyboardButtons.START_COMMAND -> {
                return requestBuilder.getStartRequest(user);
            }
            case KeyboardButtons.BACK_TO_MENU_COMMAND -> {
                return requestBuilder.getMainMenuRequest(user);
            }
            case KeyboardButtons.SETTINGS_COMMAND -> {
                return requestBuilder.getSettingsRequest(user);
            }
//            case ACCESS_COMMAND -> messageBuilder.getAccessRequest(user);
            case KeyboardButtons.PROFILE_COMMAND -> {
                return requestBuilder.getProfileRequest(user);
            }
            case KeyboardButtons.SHORT_TIME_FRAME_COMMAND -> {
                userStateService.setState(user, State.WAIT_TIME_FRAME_SHORT);
                return requestBuilder.getTimeFramesRequest(user);
            }
            case KeyboardButtons.LONG_TIME_FRAME_COMMAND -> {
                userStateService.setState(user, State.WAIT_TIME_FRAME_LONG);
                return requestBuilder.getTimeFramesRequest(user);
            }
            case KeyboardButtons.SHORT_RSI_VALUE_COMMAND -> {
                userStateService.setState(user, State.WAIT_RSI_SHORT);
                return requestBuilder.getRsiValueRequest(user);
            }
            case KeyboardButtons.LONG_RSI_VALUE_COMMAND -> {
                userStateService.setState(user, State.WAIT_RSI_LONG);
                return requestBuilder.getRsiValueRequest(user);
            }
            default -> {
                return requestBuilder.getDefaultRequest(user);
            }
        }
    }

    private void tryApplyNumericInput(AppUser user, State state, String messageText) {
        try {
            if (state.name().contains(TIME_FRAME_STRING)) {
                final TimeFrame timeFrame = TimeFrame.fromString(messageText);
                switch (state) {
                    case WAIT_TIME_FRAME_SHORT -> rsiSettingsService.updateTimeFrames(user, timeFrame, null);
                    case WAIT_TIME_FRAME_LONG -> rsiSettingsService.updateTimeFrames(user, null, timeFrame);
                }
            }
            if (state.name().contains(RSI_STRING)) {
                final BigDecimal rsiValue = new BigDecimal(messageText);
                switch (state) {
                    case WAIT_RSI_SHORT -> rsiSettingsService.updateShortRsi(user, rsiValue);
                    case WAIT_RSI_LONG -> rsiSettingsService.updateLongRsi(user, rsiValue);
                }
            }

            userStateService.setState(user, State.NONE);
            final SendMessageRequest request = requestBuilder.getSettingsUpdatedRequest(user);
            telegramClient.sendMessage(request);
        } catch (Exception ex) {
            final SendMessageRequest errorRequest = requestBuilder.getErrorRequest(user);
            telegramClient.sendMessage(errorRequest);
            log.warn("Bad numeric input '{}': {}", messageText, ex.getMessage());
        }
    }
}
