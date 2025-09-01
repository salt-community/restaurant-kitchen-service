package com.example.restaurant.kitchen_service.bootstrap;

import com.example.restaurant.kitchen_service.kafka.dto.KitchenPreparedEvent;
import com.example.restaurant.kitchen_service.kafka.producer.KitchenEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataRunner implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DemoDataRunner.class);

    private final KitchenEventProducer producer;

    public DemoDataRunner(KitchenEventProducer producer) {
        this.producer = producer;
    }

    @Override
    public void run(String... args){
        var evt = KitchenPreparedEvent.demo("order-123", "ticket-abc");
        LOG.info("Sending Kitchen Prepared Test Event: {}", evt);
        producer.publishPrepared(evt);
    }
}
