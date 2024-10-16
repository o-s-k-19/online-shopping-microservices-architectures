package com.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Getter
public class ApplicationPropertiesConfig {

	@Value("${spring.kafka.template.default-topic}")
	private String defaultTopic;
	
	@Value(value="${spring.kafka.bootstrap-servers}")
	private String bootstrapServerAdress;
}
