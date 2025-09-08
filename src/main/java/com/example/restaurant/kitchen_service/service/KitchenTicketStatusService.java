package com.example.restaurant.kitchen_service.service;

import com.example.restaurant.kitchen_service.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class KitchenTicketStatusService {

    private final TicketRepository repo;


    public KitchenTicketStatusService(TicketRepository repo) {
        this.repo = repo;
    }





}
