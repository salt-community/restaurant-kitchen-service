package com.example.restaurant.kitchen_service.kafka.dto;


import java.time.Instant;

//used for in.progress /handed.over
public record KitchenStatusEvent(
        String type,
        String ticketId,
        String orderId,
        Instant occurredAt) {}