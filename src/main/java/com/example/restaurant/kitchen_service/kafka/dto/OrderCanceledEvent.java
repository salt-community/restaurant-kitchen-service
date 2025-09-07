package com.example.restaurant.kitchen_service.kafka.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record OrderCanceledEvent(
        String orderId,
        String reason,
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant occurredAt
) {}
