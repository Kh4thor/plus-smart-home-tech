package ru.yandex.practicum.kafka.deserializer;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class SensorEventAvroDeserializer implements Deserializer<SensorEventAvro> {

    private static final Logger log = LoggerFactory.getLogger(SensorEventAvroDeserializer.class);

    private final DecoderFactory decoderFactory = DecoderFactory.get();
    private final DatumReader<SensorEventAvro> datumReader;

    public SensorEventAvroDeserializer() {
        this.datumReader = new SpecificDatumReader<>(SensorEventAvro.getClassSchema());
    }

    @Override
    public SensorEventAvro deserialize(String topic, byte[] data) {
        try {
            if (log.isTraceEnabled()) {
                log.trace("Deserializing SensorEventAvro from topic: {}, data size: {} bytes",
                        topic, data.length);
            }

            BinaryDecoder decoder = decoderFactory.binaryDecoder(data, null);
            return datumReader.read(null, decoder);

        } catch (Exception e) {
            String errorMsg = String.format(
                    "Failed to deserialize SensorEventAvro from topic [%s]. Data length: %d bytes",
                    topic, data.length);
            log.error(errorMsg, e);
            throw new SerializationException(errorMsg, e);
        }
    }
}