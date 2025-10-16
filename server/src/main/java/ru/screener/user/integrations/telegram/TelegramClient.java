package ru.screener.user.integrations.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.screener.user.dto.telegram.request.AnswerCallbackQueryRequest;
import ru.screener.user.dto.telegram.request.SendInvoiceRequest;
import ru.screener.user.dto.telegram.request.SendMessageRequest;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramClient {

    private static final String SEND_MESSAGE_METHOD = "sendMessage";
    private static final String SEND_INVOICE_METHOD = "sendInvoice";
    private static final String ANSWER_CALLBACK_METHOD = "answerCallbackQuery";
//    private static final String EDIT_MESSAGE_METHOD = "editMessageText";

    private final WebClient webClient = WebClient.builder().build();
    private final ObjectMapper objectMapper;

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

    public Mono<TgResponse<Map<String, Object>>> sendInvoiceJson(SendInvoiceRequest request) {
        return webClient.post()
                .uri(url(SEND_INVOICE_METHOD))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(resp -> resp.bodyToMono(String.class)
                        .flatMap(body -> {
                            if (resp.statusCode().is2xxSuccessful()) {
                                // Телеграм всегда возвращает 200 + {"ok":true|false,...}
                                TgResponse<Map<String, Object>> parsed =
                                        null;
                                try {
                                    parsed = objectMapper.readValue(body, new TypeReference<>() {});
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                                if (parsed.ok()) return Mono.just(parsed);
                                // ok=false, поднимем осмысленную ошибку, чтобы увидеть description
                                return Mono.error(new TelegramApiException(parsed));
                            } else {
                                // 4xx/5xx: тоже покажем тело
                                return Mono.error(new TelegramHttpException(resp.statusCode(), body));
                            }
                        }));
    }

    public record TgResponse<T>(boolean ok, T result, Integer error_code, String description) {}
    public class TelegramApiException extends RuntimeException {
        private final Integer errorCode;
        private final String raw;
        public TelegramApiException(TgResponse<?> r) {
            super("Telegram ok=false: " + r.description());
            this.errorCode = r.error_code();
            this.raw = String.valueOf(r);
        }
    }
    public class TelegramHttpException extends RuntimeException {
        public TelegramHttpException(HttpStatusCode code, String body) {
            super("Telegram HTTP " + code + ": " + body);
        }
    }

//    public TgResponse<Map<String, Object>> sendInvoice(MultiValueMap<String, String> request) {
//        return webClient.post().uri(url(SEND_INVOICE_METHOD))
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromFormData(request))
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<TgResponse<Map<String, Object>>>() {})
//                .timeout(Duration.ofSeconds(6))
//                .retryWhen(Retry.backoff(3, Duration.ofMillis(300))
//                        .filter(ex -> ex instanceof WebClientRequestException))
//                .onErrorResume(ex -> {
//                    log.warn("answerCallback failed: {}", ex.toString(), ex);
//                    return Mono.empty();
//                })
//                .block();
//    }

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
