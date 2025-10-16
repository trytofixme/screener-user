package ru.screener.user.service.telegram;

import lombok.Lombok;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.screener.user.dto.telegram.request.AnswerCallbackQueryRequest;
import ru.screener.user.dto.telegram.webhook.CallbackQuery;
import ru.screener.user.integrations.telegram.TelegramClient;
import ru.screener.user.builder.request.send.SendMessageRequestBuilder;
import ru.screener.user.service.user.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackService {

    private final SendMessageRequestBuilder messageBuilder;
    private final TelegramClient telegramClient;
    private final Integer ChatId = 341289479;

    //TODO: Разобраться с подпиской
    public Mono<Void> handleCallback(CallbackQuery callbackQuery) {
        AnswerCallbackQueryRequest answer = new AnswerCallbackQueryRequest()
                .setChatId(ChatId)
                .setTitle("Оплата")
                .setDescription("ПЛАТИ СУКА")
                .setPayload("хуй из жопы")
                .setCurrency("RUB")
                .setProviderToken("1744374395:TEST:49720cf5ccbe5ffc0a6a")
                .setPrices(List.of(new AnswerCallbackQueryRequest
                        .LabeledPrice()
                        .setLabel("1 месяц")
                        .setAmount(100)));
        return telegramClient.answerCallback(answer);
    }

    //    private void handleCallback(Map<String, Object> cb) { сделать ссылку для Оплаты
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
//                telegramClient.editMessage(new EditMessageTextRequest(                                                answerCallback
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
