package com.example.restaurant.kitchen_service.service;

import com.example.restaurant.kitchen_service.enums.TicketStatus;
import com.example.restaurant.kitchen_service.kafka.dto.*;
import com.example.restaurant.kitchen_service.kafka.producer.KitchenEventProducer;
import com.example.restaurant.kitchen_service.mapper.ItemMapper;
import com.example.restaurant.kitchen_service.model.Item;
import com.example.restaurant.kitchen_service.model.KitchenTicket;
import com.example.restaurant.kitchen_service.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketRepository repo;
    private final KitchenEventProducer producer;

    //simple policy parameters for now - adjust later in its own config file
    private final LocalTime open = LocalTime.of(10, 0);
    private final LocalTime close = LocalTime.of(21, 0);
    private final int maxConcurrentInProgress = 5;
    private final int baseMinutes = 10;
    private final int perTicketQueueMinutes = 2;

    public TicketServiceImpl(TicketRepository repo, KitchenEventProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    // consumers

    @Override
    public void onPaymentAuthorized(PaymentAuthorizedEvent event) {
        KitchenTicket ticket = repo.findByOrderId(event.orderId()).orElseGet(() -> {
            KitchenTicket t = new KitchenTicket();
            t.setOrderId(event.orderId());
            t.setStatus(TicketStatus.IN_PROGRESS);
            return repo.save(t);
        });

        switch (ticket.getStatus()) {
            case IN_PROGRESS, READY, HANDED_OVER -> {
                log.info("Ignoring payment.authorized: ticket already {} (ticketId={}, orderId={})",
                        ticket.getStatus(), ticket.getId(), ticket.getOrderId());
                return;
            }
            case CANCELED -> {
                log.warn("Ignoring payment.authorized: ticket is already CANCELED (ticketId={}, orderId={})",
                        ticket.getId(), ticket.getOrderId());
                return;
            }
        }
        producer.publishInProgress(KitchenInProgressEvent.of(
                ticket.getId().toString(),
                ticket.getOrderId()
        ));
    }


    @Override
    public void onOrderCanceled(OrderCanceledEvent event) {
        repo.findByOrderId(event.orderId()).ifPresent(ticket -> {
            if (ticket.getStatus() == TicketStatus.HANDED_OVER || ticket.getStatus() == TicketStatus.CANCELED) {
                log.info("Ignore cancel; ticket already {}", ticket.getStatus());
                return;
            }
            TicketStatus previous = ticket.getStatus();
            ticket.setStatus(TicketStatus.CANCELED);
            repo.save(ticket);

            KitchenCanceledEvent evt = KitchenCanceledEvent.of(
                    ticket.getId().toString(),
                    ticket.getOrderId(),
                    previous,
                    "ORDER_CANCELED"
            );
            producer.publishCanceled(evt);
        });
    }

    // operator/api actions

    @Override
    public void ready(UUID ticketId) {
        KitchenTicket t = mustGet(ticketId);
        ensureTransition(t.getStatus(), TicketStatus.READY);
        t.setStatus(TicketStatus.READY);
        repo.save(t);

        List<Item> items = ItemMapper.toDtos(t.getItems());
        producer.publishPrepared(KitchenPreparedEvent.of(t.getId().toString(), t.getOrderId(), items));
    }

    @Override
    public void handOver(UUID ticketId) {
        KitchenTicket t = mustGet(ticketId);
        ensureTransition(t.getStatus(), TicketStatus.HANDED_OVER);
        t.setStatus(TicketStatus.HANDED_OVER);
        repo.save(t);

        producer.publishHandedOver(KitchenHandedOverEvent.of(t.getId().toString(), t.getOrderId()));
    }

    @Override
    public void cancel(UUID ticketId, String reason) {
        KitchenTicket t = mustGet(ticketId);
        if (t.getStatus() == TicketStatus.HANDED_OVER || t.getStatus() == TicketStatus.CANCELED) {
            log.info("Ignore manual cancel; ticket already {}", t.getStatus());
            return;
        }

        String r = ("ORDER_CANCELED".equals(reason) || "OPERATOR".equals(reason)) ? reason : "OPERATOR";

        TicketStatus previous = t.getStatus();
        t.setStatus(TicketStatus.CANCELED);
        repo.save(t);

        producer.publishCanceled(KitchenCanceledEvent.of(t.getId().toString(), t.getOrderId(), previous, r));
    }

    @Override
    public void delay(UUID ticketId, int minutes, String note) {
        KitchenTicket t = mustGet(ticketId);
        Instant nowEta = (t.getEstimatedReadyAt() != null) ? t.getEstimatedReadyAt() : Instant.now();
        Instant newEta = nowEta.plusSeconds(minutes * 60L);
        t.setEstimatedReadyAt(newEta);
        repo.save(t);

        producer.publishEtaUpdated(KitchenEtaUpdatedEvent.of(t.getId().toString(), t.getOrderId(), t.getStatus(), newEta, note));
    }

    // helpers

    private KitchenTicket mustGet(UUID id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + id));
    }


    private void ensureTransition(TicketStatus from, TicketStatus to) {
        switch (to) {
            case IN_PROGRESS -> {
                if (from == TicketStatus.READY || from == TicketStatus.HANDED_OVER) {
                    throw new IllegalStateException("Cannot move to IN_PROGRESS from " + from);
                }
            }
            case READY -> {
                if (from != TicketStatus.IN_PROGRESS) {
                    throw new IllegalStateException("Must be IN_PROGRESS to mark READY");
                }
            }
            case HANDED_OVER -> {
                if (from != TicketStatus.READY) {
                    throw new IllegalStateException("Must be READY to hand over");
                }
            }
            case CANCELED -> {
                if (from == TicketStatus.HANDED_OVER) {
                    throw new IllegalStateException("Cannot cancel after HANDED_OVER");
                }
            }
            default -> {}
        }
    }
}
