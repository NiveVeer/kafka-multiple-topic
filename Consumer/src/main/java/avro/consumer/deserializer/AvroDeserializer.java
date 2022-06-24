package avro.consumer.deserializer;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;

public class AvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

	protected final Class<T> targetType;

	public AvroDeserializer(Class<T> targetType) {
		this.targetType = targetType;
	}

	@Override
	public void configure(Map configs, boolean isKey) {
		// do nothing
	}

	@Override
	public T deserialize(String topic, byte[] bytes) {
		T returnObject = null;

		try {

			if (bytes != null) {
				DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(
						targetType.newInstance().getSchema());
				System.out.println("datumReader Instance Created");
				Decoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
				System.out.println("decoder Instance Created");
				returnObject = (T) datumReader.read(null, decoder);
				System.out.println(returnObject.getClass());
				System.out.println("deserialized data= " + returnObject.toString());
			}
		} catch (Exception e) {
			System.out.println("Unable to Deserialize bytes[] " + e);
		}

		return returnObject;
	}

	@Override
	public void close() {
		// do nothing
	}
}