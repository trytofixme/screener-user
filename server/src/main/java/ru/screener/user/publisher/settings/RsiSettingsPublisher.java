package ru.screener.user.publisher.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.screener.user.config.bybit.KafkaRsiSettingsTopicProperties;
import ru.screener.user.dto.settings.RsiSettingsDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class RsiSettingsPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaRsiSettingsTopicProperties topicProperties;

    public void sendMessage(RsiSettingsDto rsiSettingsDto) {
        String key = String.valueOf(rsiSettingsDto.getTelegramId());

        kafkaTemplate.send(topicProperties.getName(), key, rsiSettingsDto)
                .thenAccept(result -> log.info(
                        "Rsi settings event {} sent to Kafka topic {} partition {} offset {}",
                        rsiSettingsDto,
                        topicProperties.getName(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset()
                ))
                .exceptionally(ex -> {
                    log.error("Failed to send rsi settings event to Kafka topic '{}'. Error: {}",
                            topicProperties.getName(), ex.getMessage(), ex);
                    return null;
                });
    }
}
