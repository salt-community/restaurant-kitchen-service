package com.example.restaurant.kitchen_service.kafka.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record PaymentAuthorizedEvent(
        String orderId,
        String paymentId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant occurredAt
) {
}
