package com.example.restaurant.kitchen_service.kafka.dto;

import com.example.restaurant.kitchen_service.model.Item;

import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
        String orderId,
        List<Item> items,
        Instant desiredPickupAt // optional, null for standard/ASAP
) {

}
