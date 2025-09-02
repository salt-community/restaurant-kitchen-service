package com.example.restaurant.kitchen_service.kafka.dto;

import java.time.Instant;

public record OrderCanceledEvent(
        String orderId,
        String reason,
        Instant occurredAt
) {}
