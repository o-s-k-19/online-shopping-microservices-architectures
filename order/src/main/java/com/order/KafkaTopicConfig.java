package com.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

	private final ApplicationPropertiesConfig applicationPropertiesConfig;
	
	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String,Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, applicationPropertiesConfig.getBootstrapServerAdress());
		return new KafkaAdmin(configs);
	}
	
	@Bean
	public NewTopic orderPlacedTopic() {
		return new NewTopic(applicationPropertiesConfig.getDefaultTopic(), 1, (short)1);
	}
}
