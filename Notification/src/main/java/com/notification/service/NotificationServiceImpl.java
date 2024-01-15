package com.notification.service;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.notification.event.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "placed_orders")
public class NotificationServiceImpl implements NotificationService{

	
	@Override
	@KafkaHandler
	public void sendNotification(OrderPlacedEvent orderPlacedEvent) {
		log.info("\n\nNotification : {}\n\n",orderPlacedEvent);
	}
	
//	@KafkaHandler(isDefault = true)
//	public void defaultValue(Object orderPlacedEvent) {
//		log.info("\n\nNotification : {}\n\n",orderPlacedEvent);
//	}

}
