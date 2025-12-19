package ru.yandex.practicum.kafka.deserializer;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SnapshotDeserializer  implements Deserializer<SensorsSnapshotAvro> {
    private static final Logger log = LoggerFactory.getLogger(SnapshotDeserializer.class);

    private final DecoderFactory decoderFactory = DecoderFactory.get();
    private final DatumReader<SensorsSnapshotAvro> datumReader;

    public SnapshotDeserializer()  {
        this.datumReader = new SpecificDatumReader<>(SensorEventAvro.getClassSchema());
    }

    @Override
    public SensorsSnapshotAvro deserialize(String topic, byte[] data) {
        try {
            if (log.isTraceEnabled()) {
                log.trace("Deserializing SensorsSnapshotAvro from topic: {}, data size: {} bytes",
                        topic, data.length);
            }

            BinaryDecoder decoder = decoderFactory.binaryDecoder(data, null);
            return datumReader.read(null, decoder);

        } catch (Exception e) {
            String errorMsg = String.format(
                    "Failed to deserialize SensorsSnapshotAvro from topic [%s]. Data length: %d bytes",
                    topic, data.length);
            log.error(errorMsg, e);
            throw new SerializationException(errorMsg, e);
        }
    }
}