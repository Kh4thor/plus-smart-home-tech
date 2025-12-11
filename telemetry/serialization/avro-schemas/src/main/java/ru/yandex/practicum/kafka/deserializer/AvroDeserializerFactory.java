package ru.yandex.practicum.kafka.deserializer;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AvroDeserializerFactory {

    @Value("${kafka.topic.sensors:telemetry.sensors.v1}")
    private String sensorsTopic;

    @Value("${kafka.topic.hubs:telemetry.hubs.v1}")
    private String hubsTopic;

    @Bean
    public Map<String, Deserializer<?>> deserializers() {
        Map<String, Deserializer<?>> deserializers = new HashMap<>();
        deserializers.put(hubsTopic, new HubEventAvroDeserializer());
        deserializers.put(sensorsTopic, new SensorEventAvroDeserializer());
        return deserializers;
    }

    @Bean
    public SensorEventAvroDeserializer sensorEventDeserializer() {
        return new SensorEventAvroDeserializer();
    }

    @Bean
    public HubEventAvroDeserializer hubEventDeserializer() {
        return new HubEventAvroDeserializer();
    }
}