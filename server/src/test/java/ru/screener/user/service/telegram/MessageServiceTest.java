package ru.screener.user.service.telegram;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.screener.user.dto.telegram.Chat;
import ru.screener.user.dto.telegram.From;
import ru.screener.user.dto.telegram.Message;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.entity.state.State;
import ru.screener.user.integrations.telegram.TelegramClient;
import ru.screener.user.integrations.telegram.request.SendMessageRequest;
import ru.screener.user.service.settings.RsiSettingsService;
import ru.screener.user.service.telegram.request.RequestBuilder;
import ru.screener.user.service.user.UserService;
import ru.screener.user.service.user.UserStateService;
import ru.screener.user.utils.keyboard.KeyboardButtons;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private UserStateService userStateService;
    @Mock
    private RsiSettingsService rsiSettingsService;
    @Mock
    private RequestBuilder requestBuilder;
    @Mock
    private TelegramClient telegramClient;

    @InjectMocks
    private MessageService messageService;

    private Message baseMessage(String text) {
        return new Message()
                .setFrom(new From().setId(1L))
                .setChat(new Chat().setId(2L))
                .setText(text);
    }

    @Test
    void handleMessageShouldIgnoreNullText() {
        Message message = baseMessage(null);

        messageService.handleMessage(message);

        verifyNoInteractions(userService, userStateService, rsiSettingsService, requestBuilder, telegramClient);
    }

    @Test
    void handleStartCommandSendsStartRequest() {
        Message message = baseMessage(KeyboardButtons.START_COMMAND);
        AppUser user = new AppUser();
        when(userService.upsertFromTelegram(anyLong(), anyLong(), any(), any(), any())).thenReturn(user);
        when(userStateService.getState(user)).thenReturn(State.NONE);
        SendMessageRequest request = new SendMessageRequest();
        when(requestBuilder.getStartRequest(user)).thenReturn(request);

        messageService.handleMessage(message);

        verify(telegramClient).sendMessage(request);
    }

    @Test
    void handleNumericInputForRsiShort() {
        Message message = baseMessage("50");
        AppUser user = new AppUser();
        when(userService.upsertFromTelegram(anyLong(), anyLong(), any(), any(), any())).thenReturn(user);
        when(userStateService.getState(user)).thenReturn(State.WAIT_RSI_SHORT);
        SendMessageRequest request = new SendMessageRequest();
        when(requestBuilder.getSettingsUpdatedRequest(user)).thenReturn(request);

        messageService.handleMessage(message);

        verify(rsiSettingsService).updateShortRsi(user, new BigDecimal("50"));
        verify(userStateService).setState(user, State.NONE);
        verify(telegramClient).sendMessage(request);
    }

    @Test
    void handleInvalidNumericInputSendsError() {
        Message message = baseMessage("oops");
        AppUser user = new AppUser();
        when(userService.upsertFromTelegram(anyLong(), anyLong(), any(), any(), any())).thenReturn(user);
        when(userStateService.getState(user)).thenReturn(State.WAIT_RSI_SHORT);
        SendMessageRequest errorRequest = new SendMessageRequest();
        when(requestBuilder.getErrorRequest(user)).thenReturn(errorRequest);

        messageService.handleMessage(message);

        verify(telegramClient).sendMessage(errorRequest);
        verify(rsiSettingsService, never()).updateShortRsi(any(), any());
    }
}
