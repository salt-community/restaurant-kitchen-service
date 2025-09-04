package com.example.restaurant.kitchen_service.service;

import com.example.restaurant.kitchen_service.kafka.dto.OrderCanceledEvent;
import com.example.restaurant.kitchen_service.kafka.dto.PaymentAuthorizedEvent;

public interface TicketAppService {
    void onOrderCanceled(OrderCanceledEvent event);
    void onPaymentAuthorized(PaymentAuthorizedEvent event);
}
