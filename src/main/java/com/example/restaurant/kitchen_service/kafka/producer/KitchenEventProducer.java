package com.example.restaurant.kitchen_service.kafka.producer;

import com.example.restaurant.kitchen_service.kafka.dto.KitchenPreparedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KitchenEventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(KitchenEventProducer.class);
    private static final String TOPIC_PREPARED = "kitchen.prepared";

    private final KafkaTemplate<String, Object> template;

    public KitchenEventProducer(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    public void publishPrepared(KitchenPreparedEvent event){

        String key = event.orderId();
        template.send(TOPIC_PREPARED, key, event)
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
}
