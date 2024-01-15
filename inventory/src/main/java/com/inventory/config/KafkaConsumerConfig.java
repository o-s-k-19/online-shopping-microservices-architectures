package com.inventory.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.inventory.event.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumerConfig {

	private final ApplicationPropertiesConfig applicationPropertiesConfig;
	
	@Bean
	public ConsumerFactory<String, OrderPlacedEvent> consumerFactoryForOrderPlacedEvent(){
		Map<String,Object> configs = new HashMap<>();
		
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationPropertiesConfig.getBootstrapServerAdress());
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, applicationPropertiesConfig.getConsumerGroupId());
		
		return new DefaultKafkaConsumerFactory<>(configs,
				new StringDeserializer(),new JsonDeserializer<>(OrderPlacedEvent.class, false));
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String,OrderPlacedEvent> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String,OrderPlacedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryForOrderPlacedEvent());
		return factory;
	}
}
