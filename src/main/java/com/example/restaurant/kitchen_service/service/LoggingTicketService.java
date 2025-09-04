package com.example.restaurant.kitchen_service.service;

import com.example.restaurant.kitchen_service.kafka.dto.OrderCanceledEvent;
import com.example.restaurant.kitchen_service.kafka.dto.OrderCreatedEvent;
import com.example.restaurant.kitchen_service.kafka.dto.PaymentAuthorizedEvent;
import com.example.restaurant.kitchen_service.kafka.dto.PaymentFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class LoggingTicketService implements TicketAppService {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingTicketService.class);

    @Override
    public void onOrderCanceled(OrderCanceledEvent event) {
        LOG.info("[ticket] order.canceled received -> cancel ticket for orderId={} reason={}",
                event.orderId(), event.reason());
        // TODO: cancel if not handed over, produce kitchen.canceled
    }

    @Override
    public void onPaymentAuthorized(PaymentAuthorizedEvent event) {
        LOG.info("[ticket] payment.authorized received -> evaluate hours/capacity for orderId={}",
                event.orderId());
        // TODO: accept or cancel(reason=CLOSED|CAPACITY), produce event
    }
}
