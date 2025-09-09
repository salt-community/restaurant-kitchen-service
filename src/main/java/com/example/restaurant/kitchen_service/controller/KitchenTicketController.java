package com.example.restaurant.kitchen_service.controller;

import com.example.restaurant.kitchen_service.dto.response.TicketStatusResponse;
import com.example.restaurant.kitchen_service.service.KitchenTicketStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class KitchenTicketController {


    private final KitchenTicketStatusService statusService;

    public KitchenTicketController(KitchenTicketStatusService statusService) {
        this.statusService = statusService;
    }


    @GetMapping("/{orderId}/status")
    public ResponseEntity<TicketStatusResponse> getByOrderId(@PathVariable String orderId) {
        return statusService.findByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



}
