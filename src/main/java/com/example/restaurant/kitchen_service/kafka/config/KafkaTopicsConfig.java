package com.example.restaurant.kitchen_service.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic kitchenPrepared() {
        return TopicBuilder.name("kitchen.prepared")
                .partitions(6)
                .replicas(1)
                .build();
    }
}
