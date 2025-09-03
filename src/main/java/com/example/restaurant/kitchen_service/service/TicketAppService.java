package com.example.restaurant.kitchen_service.service;

import com.example.restaurant.kitchen_service.kafka.dto.OrderCanceledEvent;
import com.example.restaurant.kitchen_service.kafka.dto.OrderCreatedEvent;
import com.example.restaurant.kitchen_service.kafka.dto.PaymentAuthorizedEvent;
import com.example.restaurant.kitchen_service.kafka.dto.PaymentFailedEvent;

public interface TicketAppService {
    void onOrderCanceled(OrderCanceledEvent event);
    void onPaymentAuthorized(PaymentAuthorizedEvent event);
}
