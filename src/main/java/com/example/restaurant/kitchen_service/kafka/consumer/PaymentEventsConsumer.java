package com.example.restaurant.kitchen_service.kafka.consumer;

import com.example.restaurant.kitchen_service.kafka.dto.PaymentAuthorizedEvent;
import com.example.restaurant.kitchen_service.service.TicketService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//no group id on methods, ATM it's sorted out in application.yml

@Component
public class PaymentEventsConsumer {

    private final TicketService ticketService;

    public PaymentEventsConsumer(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @KafkaListener(topics = "payment.authorized")
    public void onAuthorized(PaymentAuthorizedEvent event) {
        ticketService.onPaymentAuthorized(event);
    }

}
