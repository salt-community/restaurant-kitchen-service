package com.example.restaurant.kitchen_service.kafka.dto;

import java.time.Instant;

public record PaymentAuthorizedEvent(
        String orderId,
        String paymentId,
        Instant occurredAt
) {
}
