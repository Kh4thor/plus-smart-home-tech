package ru.yandex.practicum.telemetry.collector.kafka;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.scenario.ScenarioEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.sensor.SensorEventAvro;

public class GeneralAvroDeserializer implements Deserializer<SpecificRecordBase> {
    private final DecoderFactory decoderFactory = DecoderFactory.get();

    @Override
    public SpecificRecordBase deserialize(String topic, byte[] bytes) {
        try {
            if (bytes != null) {
                BinaryDecoder decoder = decoderFactory.binaryDecoder(bytes, null);
                DatumReader<SpecificRecordBase> reader;

                switch (topic) {
                    case EventTopic.DEVICE_EVENTS:
                        reader = new SpecificDatumReader<>(DeviceEventAvro.getClassSchema());
                        break;
                    case EventTopic.SENSOR_EVENTS:
                        reader = new SpecificDatumReader<>(SensorEventAvro.getClassSchema());
                        break;
                    case EventTopic.SCENARIO_EVENTS:
                        reader = new SpecificDatumReader<>(ScenarioEventAvro.getClassSchema());
                        break;
                    default:
                        throw new IllegalArgumentException("Неизвестный топик: " + topic);
                }

                return reader.read(null, decoder);
            }
            return null;
        } catch (Exception e) {
            throw new SerializationException("Ошибка десереализации данных из топика [" + topic + "]", e);
        }
    }
}