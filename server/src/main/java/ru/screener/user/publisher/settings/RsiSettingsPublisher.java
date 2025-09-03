package ru.screener.user.publisher.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.screener.user.config.bybit.KafkaRsiSettingsTopicProperties;
import ru.screener.user.dto.settings.RsiSettingsDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class RsiSettingsPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaRsiSettingsTopicProperties topicProperties;

    public void sendMessage(RsiSettingsDto rsiSettingsDto) {
        kafkaTemplate.send(topicProperties.getName(), rsiSettingsDto).thenAccept(arg ->
                        log.info("Rsi settings event {} sent to Kafka topic {}", rsiSettingsDto, topicProperties.getName()))
        .exceptionally(ex -> {
            log.error("Failed to send rsi settings event to Kafka topic '{}'. Error: {}",
            topicProperties.getName(), ex.getMessage());
            return null;
        });
    }
}
