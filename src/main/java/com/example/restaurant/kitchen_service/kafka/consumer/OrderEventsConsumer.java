package com.example.restaurant.kitchen_service.kafka.consumer;

import com.example.restaurant.kitchen_service.kafka.dto.OrderCanceledEvent;
import com.example.restaurant.kitchen_service.kafka.dto.OrderCreatedEvent;
import com.example.restaurant.kitchen_service.service.TicketAppService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//no group id on methods, ATM it's sorted out in application.yml

@Component
public class OrderEventsConsumer {

    private final TicketAppService ticketService;

    public OrderEventsConsumer(TicketAppService ticketService) {
        this.ticketService = ticketService;
    }

    @KafkaListener(topics = "order.canceled")
    public void onOrderCanceled(OrderCanceledEvent event){
        ticketService.onOrderCanceled(event);
    }


}
