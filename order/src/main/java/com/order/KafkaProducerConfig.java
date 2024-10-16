package com.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.serializer.StringOrBytesSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
	
	private final ApplicationPropertiesConfig applicationPropertiesConfig;

	@Bean
	public ProducerFactory<String, OrderPlacedEvent> producerFactoryForOrderPlacedEvent(){
		Map<String, Object> configs = new HashMap<String, Object>();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationPropertiesConfig.getBootstrapServerAdress());
		JsonSerializer<OrderPlacedEvent> jsonSerializer = new JsonSerializer<OrderPlacedEvent>();
		jsonSerializer.setAddTypeInfo(true);
		return new DefaultKafkaProducerFactory<>(configs,
				new StringSerializer(),jsonSerializer);
	}
	
	@Bean
	public KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate(){
		return new KafkaTemplate<String, OrderPlacedEvent>(producerFactoryForOrderPlacedEvent());
	}
}
