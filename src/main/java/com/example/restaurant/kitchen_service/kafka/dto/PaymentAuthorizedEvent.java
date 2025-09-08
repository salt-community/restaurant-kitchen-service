package com.example.restaurant.kitchen_service.kafka.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.List;

public record PaymentAuthorizedEvent(
        String orderId,
        String paymentId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant occurredAt,
        List<ReceivedRecipeDto> orderedRecipesDto
) {
    public record ReceivedRecipeDto(Integer itemId, Integer quantity) {
    }
}
