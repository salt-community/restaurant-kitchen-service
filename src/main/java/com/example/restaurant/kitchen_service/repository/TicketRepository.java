package com.example.restaurant.kitchen_service.repository;

import com.example.restaurant.kitchen_service.enums.TicketStatus;
import com.example.restaurant.kitchen_service.model.KitchenTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<KitchenTicket, UUID> {
    Optional<KitchenTicket> findByOrderId(String orderId);
    List<KitchenTicket> findByStatus(TicketStatus status);
}
