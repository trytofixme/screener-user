package ru.screener.user.integrations.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.screener.user.model.dto.AnswerCallbackQueryRequest;
import ru.screener.user.model.dto.EditMessageTextRequest;
import ru.screener.user.model.dto.SendMessageRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramClient {

    private static final String SEND_MESSAGE_METHOD = "sendMessage";
    private static final String ANSWER_CALLBACK_METHOD = "answerCallbackQuery";
    private static final String EDIT_MESSAGE_METHOD = "editMessageText";

    private final WebClient webClient = WebClient.builder().build();

    @Value("${telegram.api-base-url}")
    private String apiBase;

    @Value("${telegram.bot-token}")
    private String token;

    private String url(String method) {
        return String.format("%s/bot%s/%s", apiBase, token, method);
    }

    public void sendMessage(SendMessageRequest request) {
        webClient.post().uri(url(SEND_MESSAGE_METHOD))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class).flatMap(body -> {
                            log.error("Telegram sendMessage error {}: {}", resp.statusCode(), body);
                            return Mono.error(new RuntimeException("Telegram error: " + body));
                        })
                )
                .bodyToMono(String.class)
                .block();
    }

    public void answerCallback(AnswerCallbackQueryRequest request) {
        webClient.post().uri(url(ANSWER_CALLBACK_METHOD))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class).flatMap(body -> {
                            log.error("Telegram sendMessage error {}: {}", resp.statusCode(), body);
                            return Mono.error(new RuntimeException("Telegram error: " + body));
                        })
                )
                .bodyToMono(String.class)
                .block();
    }

    public void editMessage(EditMessageTextRequest request) {
        webClient.post().uri(url(EDIT_MESSAGE_METHOD))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class).flatMap(body -> {
                            log.error("Telegram sendMessage error {}: {}", resp.statusCode(), body);
                            return Mono.error(new RuntimeException("Telegram error: " + body));
                        })
                )
                .bodyToMono(String.class)
                .block();
    }
}
