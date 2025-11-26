package ru.yandex.practicum.telemetry.collector.kafka;

import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Getter
@ToString
@ConfigurationProperties("collector.kafka.producer")
public class KafkaConfig {
    private final Properties properties;
    private final Map<String, String> topics = new HashMap<>();

    public KafkaConfig(Properties properties, Map<String, String> topics) {
        this.properties = properties;

        this.topics.put(EventTopic.DEVICE_EVENTS, EventTopic.DEVICE_EVENTS);
        this.topics.put(EventTopic.SENSOR_EVENTS, EventTopic.SENSOR_EVENTS);
        this.topics.put(EventTopic.SCENARIO_EVENTS, EventTopic.SCENARIO_EVENTS);

        if (topics != null) {
            this.topics.putAll(topics);
        }
    }
}