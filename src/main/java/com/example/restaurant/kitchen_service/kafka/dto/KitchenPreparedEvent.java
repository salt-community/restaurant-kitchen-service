package com.example.restaurant.kitchen_service.kafka.dto;

import com.example.restaurant.kitchen_service.enums.TicketStatus;
import com.example.restaurant.kitchen_service.model.Item;
import com.example.restaurant.kitchen_service.model.Recipe;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record KitchenPreparedEvent(
        String eventId,
        String ticketId,
        String orderId,
        TicketStatus status,
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant occurredAt,
        List<Recipe> craftedRecipes
) implements KitchenEvent {
    public static KitchenPreparedEvent of(String ticketId, String orderId, List<Recipe> craftedRecipes) {
        return new KitchenPreparedEvent(
                UUID.randomUUID().toString(),
                ticketId,
                orderId,
                TicketStatus.READY,
                Instant.now(),
                craftedRecipes
        );
    }
}
