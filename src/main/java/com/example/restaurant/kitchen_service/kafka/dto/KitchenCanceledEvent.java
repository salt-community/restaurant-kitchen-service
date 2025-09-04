package com.example.restaurant.kitchen_service.kafka.dto;
import java.time.Instant;
import java.util.UUID;
public record KitchenCanceledEvent(
        String eventId,
        String ticketId,
        String orderId,
        String stage,
        String reason,
        Instant occurredAt) {

    public static KitchenCanceledEvent of(
            String ticketId,
            String orderId,
            String stage,
            String reason) {

        return new KitchenCanceledEvent(
                UUID.randomUUID().toString(),
                ticketId,
                orderId,
                stage,
                reason,
                Instant.now());
    }
}
