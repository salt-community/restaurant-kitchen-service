package com.example.restaurant.kitchen_service.kafka.dto;

import com.example.restaurant.kitchen_service.enums.TicketStatus;

public interface KitchenEvent {
    String eventId();
    String ticketId();
    String orderId();
    TicketStatus status();
}
