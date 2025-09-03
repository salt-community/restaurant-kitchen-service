package com.example.restaurant.kitchen_service.service;

import com.example.restaurant.kitchen_service.kafka.dto.OrderCanceledEvent;
import com.example.restaurant.kitchen_service.kafka.dto.PaymentAuthorizedEvent;

import java.util.UUID;

public interface TicketService {
    void onOrderCanceled(OrderCanceledEvent event);
    void onPaymentAuthorized(PaymentAuthorizedEvent event);

    //for rest
    void start(UUID ticketId);
    void ready(UUID ticketId);
    void handOver(UUID ticketId);
    void cancel(UUID ticketId, String reason);
    void delay(UUID ticketId, int minutes, String note);
}
