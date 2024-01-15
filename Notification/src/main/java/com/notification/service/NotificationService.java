package com.notification.service;

import com.notification.event.OrderPlacedEvent;

public interface NotificationService {

	void sendNotification(OrderPlacedEvent orderPlacedEvent);
}
