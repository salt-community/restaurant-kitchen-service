package com.example.restaurant.kitchen_service.kafka.consumer;

import com.example.restaurant.kitchen_service.kafka.dto.PaymentAuthorizedEvent;
import com.example.restaurant.kitchen_service.kafka.dto.PaymentFailedEvent;
import com.example.restaurant.kitchen_service.service.TicketAppService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//no group id on methods, ATM it's sorted out in application.yml

@Component
public class PaymentEventsConsumer {

    private final TicketAppService ticketService;

    public PaymentEventsConsumer(TicketAppService ticketService) {
        this.ticketService = ticketService;
    }

    @KafkaListener(topics = "payment.authorized")
    public void onAuthorized(PaymentAuthorizedEvent event) {
        ticketService.onPaymentAuthorized(event);
    }

    @KafkaListener(topics = "payment.failed")
    public void onFailed(PaymentFailedEvent event) {
        ticketService.onPaymentFailed(event);
    }
}
