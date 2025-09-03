package ru.screener.user.service.settings;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.screener.user.dto.settings.RsiSettingsDto;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.repository.user.AppUserRepository;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "settings.rsi", bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@ActiveProfiles("test")
@DirtiesContext
class RsiSettingsServiceKafkaIntegrationTest {

    @Autowired
    private RsiSettingsService service;
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    private Consumer<String, RsiSettingsDto> consumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> props = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        JsonDeserializer<RsiSettingsDto> deserializer = new JsonDeserializer<>(RsiSettingsDto.class);
        deserializer.addTrustedPackages("*");
        consumer = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer).createConsumer();
        embeddedKafka.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    void createDefaultSettingsPublishesEventToKafka() {
        AppUser user = new AppUser();
        user.setTgUserId(42L);
        user.setChatId(7L);
        userRepository.save(user);

        service.createDefaultSettings(user);

        var record = KafkaTestUtils.getSingleRecord(consumer, "settings.rsi");
        assertThat(record.value().getTelegramId()).isEqualTo(42L);
    }
}
