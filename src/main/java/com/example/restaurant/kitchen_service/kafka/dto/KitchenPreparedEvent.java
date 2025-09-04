package com.example.restaurant.kitchen_service.kafka.dto;

import com.example.restaurant.kitchen_service.model.Item;

import java.time.Instant;
import java.util.List;

public record KitchenPreparedEvent(
        String eventId,
        String ticketId,
        String orderId,
        String status,
        Instant occurredAt,
        List<Item> items
) {
/*
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
*/



}
