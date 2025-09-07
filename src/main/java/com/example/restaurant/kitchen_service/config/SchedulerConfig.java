package com.example.restaurant.kitchen_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

//used for the autopilot with timers, drop these once we have functional frontend for user, staff, etc
@Configuration
public class SchedulerConfig {

    @Bean
    public TaskScheduler taskScheduler() {}
}
