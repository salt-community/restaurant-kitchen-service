package com.example.restaurant.kitchen_service.kafka.dto;

import java.time.Instant;

public record KitchenEtaUpdatedEvent(String ticketId,
                                     String orderId,
                                     Instant estimatedReadyAt,
                                     String note) {}