package com.example.restaurant.kitchen_service.kafka.producer;

import com.example.restaurant.kitchen_service.kafka.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KitchenEventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(KitchenEventProducer.class);

    private final KafkaTemplate<String, Object> template;

    public KitchenEventProducer(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    public void publishAccepted(KitchenAcceptedEvent event) {

        String key = event.orderId();
        template.send("kitchen.accepted", key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        LOG.error("Failed to publish kitchen.accepted for orderId={}", key, ex);
                    } else {
                        LOG.info("Published kitchen.accepted key={} partition={} offset={}",
                                key,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    public void publishCanceled(KitchenCanceledEvent event){

        String key = event.orderId();
        template.send("kitchen.canceled", key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        LOG.error("Failed to publish kitchen.canceled for orderId={}", key, ex);
                    } else {
                        LOG.info("Published kitchen.canceled key={} partition={} offset={}",
                                key,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    public void publishInProgress(KitchenStatusEvent event){

        String key = event.orderId();
        template.send("kitchen.in.progress", key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        LOG.error("Failed to publish kitchen.in.progress for orderId={}", key, ex);
                    } else {
                        LOG.info("Published kitchen.in.progress key={} partition={} offset={}",
                                key,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    public void publishEtaUpdated(KitchenEtaUpdatedEvent event){

        String key = event.orderId();
        template.send("kitchen.eta.updated", key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        LOG.error("Failed to publish kitchen.eta.updated for orderId={}", key, ex);
                    } else {
                        LOG.info("Published kitchen.eta.updated key={} partition={} offset={}",
                                key,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }



    public void publishPrepared(KitchenPreparedEvent event){

        String key = event.orderId();
        template.send("kitchen.prepared", key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        LOG.error("Failed to publish kitchen.prepared for orderId={}", key, ex);
                    } else {
                        LOG.info("Published kitchen.prepared key={} partition={} offset={}",
                                key,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    public void publishHandedOver(KitchenStatusEvent event){

        String key = event.orderId();
        template.send("kitchen.handed_over", key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        LOG.error("Failed to publish kitchen.handed.over for orderId={}", key, ex);
                    } else {
                        LOG.info("Published kitchen.handed.over key={} partition={} offset={}",
                        key,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                    }
                });

    }

}
