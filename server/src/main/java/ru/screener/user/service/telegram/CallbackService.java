package ru.screener.user.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import ru.screener.user.helpers.RequestBuilder;
import ru.screener.user.integrations.telegram.TelegramClient;
import ru.screener.user.dto.telegram.CallbackQuery;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackService {

    private final RequestBuilder messageBuilder;
    private final TelegramClient telegramClient;

    public void handleCallback(CallbackQuery callbackQuery) {
        throw new NotImplementedException();
    }

    //    private void handleCallback(Map<String, Object> cb) {
//        Map<String, Object> from = (Map<String, Object>) cb.get("from");
//        Map<String, Object> msg = (Map<String, Object>) cb.get("message");
//        String data = (String) cb.get("data");
//
//        AppUser user = userService.upsertFromTelegram(
//                ((Number) from.get("id")).longValue(),
//                ((Number)((Map<String,Object>)msg.get("chat")).get("id")).longValue(),
//                (String) from.get("username"),
//                (String) from.get("first_name"),
//                (String) from.get("last_name")
//        );
//
//        Integer messageId = ((Number) msg.get("message_id")).intValue();
//
//        switch (data) {
//            case "EXCH_TOGGLE_BYBIT" -> {
//                var e = userService.toggleBybit(user);
//                telegramClient.editMessage(new EditMessageTextRequest(
//                        user.getChatId(), messageId,
//                        exchangesText(), Keyboards.exchangesInline(e.isBybitEnabled(), e.isMoexEnabled()), PARSE));
//            }
//            case "EXCH_TOGGLE_MOEX" -> {
//                var e = userService.toggleMoex(user);
//                telegramClient.editMessage(new EditMessageTextRequest(
//                        user.getChatId(), messageId,
//                        exchangesText(), Keyboards.exchangesInline(e.isBybitEnabled(), e.isMoexEnabled()), PARSE));
//            }
//            case "SET_PERIOD_LONG"  -> userService.setState(user, UserState.State.WAIT_PERIOD_LONG);
//            case "SET_PERIOD_SHORT" -> userService.setState(user, UserState.State.WAIT_PERIOD_SHORT);
//            case "SET_PERIOD_DUMP"  -> userService.setState(user, UserState.State.WAIT_PERIOD_DUMP);
//            case "SET_PERCENT_LONG" -> userService.setState(user, UserState.State.WAIT_PERCENT_LONG);
//            case "SET_PERCENT_SHORT"-> userService.setState(user, UserState.State.WAIT_PERCENT_SHORT);
//            case "SET_PERCENT_DUMP" -> userService.setState(user, UserState.State.WAIT_PERCENT_DUMP);
//            case "BACK_MAIN" -> sendMainMenu(user);
//            default -> {}
//        }
//        telegramClient.answerCallback(new AnswerCallbackQueryRequest(
//                (String) cb.get("id"), null, false));
//        if (data.startsWith("SET_")) {
//            telegramClient.sendMessage(new SendMessageRequest(
//                    user.getChatId(),
//                    "Введи числовое значение (только число).",
//                    null, PARSE, true));
//        }
//    }
}
