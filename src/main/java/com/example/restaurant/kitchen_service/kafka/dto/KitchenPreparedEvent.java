package com.example.restaurant.kitchen_service.kafka.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record KitchenPreparedEvent(
        String eventId,
        String ticketId,
        String orderId,
        String status,
        Instant ocurredAt,
        List<Item> items
) {

    public static KitchenPreparedEvent demo(String orderId, String ticketId) {

        return new KitchenPreparedEvent(
                UUID.randomUUID().toString(),
                ticketId,
                orderId,
                "PREPARED",
                Instant.now(),
                List.of(new Item("burger-123", 2))
        );
    }

    public record Item(String sku, int qty){}



}
