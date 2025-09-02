package com.example.restaurant.kitchen_service.kafka.dto;

import java.time.Instant;

public record PaymentFailedEvent(
        String orderId,
        String paymentId,
        String reason,  //for example INSUFFICIENT_FUNDS or something
        Instant occurredAt
) {
}
