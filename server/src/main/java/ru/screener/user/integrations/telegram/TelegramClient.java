package ru.screener.user.integrations.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.screener.user.dto.telegram.request.AnswerCallbackQueryRequest;
import ru.screener.user.dto.telegram.request.AnswerPreCheckoutRequest;
import ru.screener.user.dto.telegram.request.SendInvoiceRequest;
import ru.screener.user.dto.telegram.request.SendMessageRequest;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramClient {

    private static final String SEND_MESSAGE_METHOD = "sendMessage";
    private static final String SEND_INVOICE_METHOD = "sendInvoice";
    private static final String ANSWER_CALLBACK_METHOD = "answerCallbackQuery";
    private static final String ANSWER_PRE_CHECKOUT_METHOD = "answerPreCheckoutQuery";
//    private static final String EDIT_MESSAGE_METHOD = "editMessageText";

    private final WebClient webClient = WebClient.builder().build();

    @Value("${telegram.api-base-url}")
    private String apiBase;

    @Value("${telegram.bot-token}")
    private String token;

    private String url(String method) {
        return String.format("%s/bot%s/%s", apiBase, token, method);
    }

    public Mono<Void> sendMessage(SendMessageRequest request) {
        return webClient.post().uri(url(SEND_MESSAGE_METHOD))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(6))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(300))
                        .filter(ex -> ex instanceof WebClientRequestException))
                .onErrorResume(ex -> {
                    log.warn("sendMessage failed: {}", ex.toString(), ex);
                    return Mono.empty();
                });
    }

    public Mono<Void> sendInvoice(SendInvoiceRequest request) {
        return webClient.post()
                .uri(url(SEND_INVOICE_METHOD))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(6))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(300))
                        .filter(ex -> ex instanceof WebClientRequestException))
                .onErrorResume(ex -> {
                    log.warn("sendInvoice failed: {}", ex.toString(), ex);
                    return Mono.empty();
                });
    }

    public Mono<Void> answerPreCheckout(AnswerPreCheckoutRequest request) {
        return webClient.post().uri(url(ANSWER_PRE_CHECKOUT_METHOD))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(6))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(300))
                        .filter(ex -> ex instanceof WebClientRequestException))
                .onErrorResume(ex -> {
                    log.warn("sendMessage failed: {}", ex.toString(), ex);
                    return Mono.empty();
                });
    }

    public Mono<Void> answerCallback(AnswerCallbackQueryRequest request) {
        return webClient.post().uri(url(ANSWER_CALLBACK_METHOD))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(6))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(300))
                        .filter(ex -> ex instanceof WebClientRequestException))
                .onErrorResume(ex -> {
                    log.warn("answerCallback failed: {}", ex.toString(), ex);
                    return Mono.empty();
                });
    }

//
//    public void editMessage(EditMessageTextRequest request) {
//        webClient.post().uri(url(EDIT_MESSAGE_METHOD))
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .retrieve()
//                .onStatus(HttpStatusCode::isError, resp ->
//                        resp.bodyToMono(String.class).flatMap(body -> {
//                            log.error("Telegram sendMessage error {}: {}", resp.statusCode(), body);
//                            return Mono.error(new RuntimeException("Telegram error: " + body));
//                        })
//                )
//                .bodyToMono(String.class)
//                .block();
//    }
}
