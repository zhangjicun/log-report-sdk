package cn.com.tvmore.logreport.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.SettableFuture;

import cn.com.tvmore.logreport.constants.Constants;
import cn.com.tvmore.logreport.properties.PropBank;

/**
 * Kafka Producer(utility class)
 * do send to kafka cluster
 */
public class KafkaSender {
	private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);
	
	private static KafkaProducer<String, String> producer = null;
	
	private static boolean autoFlush;
	
	/**
	 * Initializes the KafkaProducer
	 */
	static{
		Map<String, Object> props = new HashMap<>(); 
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, PropBank.getProperty(Constants.PROP_KAFKA_BOOTSTRAP_SERVERS, "172.16.13.1:9092,172.16.13.2:9092,172.16.13.3:9092"));
		props.put(ProducerConfig.ACKS_CONFIG, PropBank.getProperty(Constants.PROP_KAFKA_ACKS,"all"));
		props.put(ProducerConfig.RETRIES_CONFIG, PropBank.getProperty(Constants.PROP_KAFKA_RETRIES, "1"));
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, PropBank.getProperty(Constants.PROP_KAFKA_BATCHSIZE, "16384"));
	    props.put(ProducerConfig.LINGER_MS_CONFIG, PropBank.getProperty(Constants.PROP_KAFKA_LINGERMS, "1"));
	    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, PropBank.getProperty(Constants.PROP_KAFKA_BUUFFER, "33554432"));
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    
	    
	    producer = new KafkaProducer<String, String>(props);
	}
	
	/**
	 * Send kafka message with topic and key
	 * @param topic 
	 * @param key
	 * @param value
	 * @return
	 */
	public static Future<RecordMetadata> send(String topic, String key, String value) {
		ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
		/*Future<RecordMetadata> future = producer.send(record);*/
		Future<RecordMetadata> future = dosend(record);
		return future;
	}
	
	/**
	 * Send kafka message without key
	 * @param topic 
	 * @param value
	 * @return
	 */
	public static Future<RecordMetadata> send(String topic, String value) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, value);
		/*Future<RecordMetadata> future = producer.send(record);*/
		Future<RecordMetadata> future = dosend(record);
		return future;
	}
	
	/**
	 * Specific sending logic
	 * @param record
	 * @return
	 */
	protected static Future<RecordMetadata> dosend(final ProducerRecord<String, String> record) {
		final SettableFuture<RecordMetadata> future = SettableFuture.create();
		producer.send(record, new Callback() {
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				try{
					if(exception == null) {
						logger.info("[KAFKA_SUCCESS]Send kafka message SUCCESS==>topic:{},partion:{},key:{},value:{},offset:{}",record.topic(),record.partition(),record.key(),record.value(),metadata.offset());
					} else {
						logger.error(exception.getMessage());
						logger.error("[KAFKA_FAIL]Send kafka message FAIL==>topic:{},partion:{},key:{},value:{}",record.topic(),record.partition(),record.key(),record.value());
					}
					future.set(metadata);
				}
				finally {
					/** Cannot be closed, because one process has only one client*/
					/*producer.close();*/
				}
			}
		});
		if(autoFlush) {
			producer.flush();
		}
		return future;
	}
	
}
