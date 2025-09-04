package com.example.restaurant.kitchen_service.kafka.dto;

import java.time.Instant;
import java.util.UUID;

public record KitchenAcceptedEvent(String eventId,
                                   String ticketId,
                                   String orderId,
                                   Instant estimatedReadyAt,
                                   Instant occurredAt) {
    public static KitchenAcceptedEvent of(String ticketId,
                                          String orderId,
                                          Instant eta) {
        return new KitchenAcceptedEvent(
                UUID.randomUUID().toString(),
                ticketId,
                orderId,
                eta,
                Instant.now());
    }
}
