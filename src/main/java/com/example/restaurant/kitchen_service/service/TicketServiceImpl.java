package com.example.restaurant.kitchen_service.service;

import com.example.restaurant.kitchen_service.kafka.dto.KitchenAcceptedEvent;
import com.example.restaurant.kitchen_service.kafka.dto.KitchenCanceledEvent;
import com.example.restaurant.kitchen_service.kafka.dto.KitchenPreparedEvent;
import com.example.restaurant.kitchen_service.kafka.producer.KitchenEventProducer;
import com.example.restaurant.kitchen_service.mapper.ItemMapper;
import com.example.restaurant.kitchen_service.model.KitchenTicket;
import com.example.restaurant.kitchen_service.model.TicketItemEntity;
import com.example.restaurant.kitchen_service.enums.TicketStatus;
import com.example.restaurant.kitchen_service.repository.TicketRepository;
import com.example.restaurant.kitchen_service.kafka.dto.*;

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
        var ticket = repo.findByOrderId(event.orderId())
                .orElseThrow(() -> new IllegalStateException("No ticket for orderId=" + event.orderId()));

        // policy, like if it's open and if we have capacity
        if (!isOpenNow()) {
            cancelInternal(ticket, "CLOSED", "PENDING");
            return;
        }
        if (!hasCapacity()) {
            cancelInternal(ticket, "CAPACITY", "PENDING");
            return;
        }
        // Accept: we keep the status as QUEUED (accepted and in the queue) and compute the ETA
        var eta = computeEta();
        ticket.setEstimatedReadyAt(eta);
        repo.save(ticket);

        var evt = KitchenAcceptedEvent.of(ticket.getId().toString(), ticket.getOrderId(), eta);
        producer.publishAccepted(evt);
        log.info("Accepted ticket id={} orderId={} eta={}", ticket.getId(), ticket.getOrderId(), eta);
    }

    @Override
    public void onOrderCanceled(OrderCanceledEvent event) {
        repo.findByOrderId(event.orderId()).ifPresent(ticket -> {
            if (ticket.getStatus() != TicketStatus.HANDED_OVER && ticket.getStatus() != TicketStatus.CANCELED) {
                cancelInternal(ticket, "ORDER_CANCELED", currentStage(ticket));
            } else {
                log.info("Ignore cancel; ticket already {}", ticket.getStatus());
            }
        });
    }

    @Override
    public void onPaymentFailed(PaymentFailedEvent event) {
        repo.findByOrderId(event.orderId()).ifPresent(ticket -> {
            if (ticket.getStatus() != TicketStatus.HANDED_OVER && ticket.getStatus() != TicketStatus.CANCELED) {
                cancelInternal(ticket, "PAYMENT_FAILED", currentStage(ticket));
            } else {
                log.info("Ignore payment.failed; ticket already {}", ticket.getStatus());
            }
        });
    }

    // operator/api actions

    @Override
    public void start(UUID ticketId) {
        var t = mustGet(ticketId);
        ensureTransition(t.getStatus(), TicketStatus.IN_PROGRESS);
        t.setStatus(TicketStatus.IN_PROGRESS);
        repo.save(t);
        producer.publishInProgress(new KitchenStatusEvent("kitchen.in.progress", t.getId().toString(), t.getOrderId(), Instant.now()));
    }

    @Override
    public void ready(UUID ticketId) {
        var t = mustGet(ticketId);
        ensureTransition(t.getStatus(), TicketStatus.READY);
        t.setStatus(TicketStatus.READY);
        repo.save(t);

        var evt = KitchenPreparedEvent.demo(t.getOrderId(), "ticket-" + t.getId()); // byt till egen factory om du vill
        producer.publishPrepared(evt);
    }

    @Override
    public void handOver(UUID ticketId) {
        var t = mustGet(ticketId);
        ensureTransition(t.getStatus(), TicketStatus.HANDED_OVER);
        t.setStatus(TicketStatus.HANDED_OVER);
        repo.save(t);

        producer.publishHandedOver(new KitchenStatusEvent("kitchen.handed.over", t.getId().toString(), t.getOrderId(), Instant.now()));
    }

    @Override
    public void cancel(UUID ticketId, String reason) {
        var t = mustGet(ticketId);
        if (t.getStatus() == TicketStatus.HANDED_OVER || t.getStatus() == TicketStatus.CANCELED) {
            log.info("Ignore manual cancel; ticket already {}", t.getStatus());
            return;
        }
        cancelInternal(t, reason, currentStage(t));
    }

    @Override
    public void delay(UUID ticketId, int minutes, String note) {
        var t = mustGet(ticketId);
        var nowEta = t.getEstimatedReadyAt() != null ? t.getEstimatedReadyAt() : Instant.now();
        var newEta = nowEta.plusSeconds(minutes * 60L);
        t.setEstimatedReadyAt(newEta);
        repo.save(t);
        producer.publishEtaUpdated(new KitchenEtaUpdatedEvent(t.getId().toString(), t.getOrderId(), newEta, note));
    }

    // helpers

    private KitchenTicket mustGet(UUID id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + id));
    }

    private void cancelInternal(KitchenTicket t, String reason, String stage) {
        t.setStatus(TicketStatus.CANCELED);
        repo.save(t);
        var evt = KitchenCanceledEvent.of(t.getId().toString(), t.getOrderId(), stage, reason);
        producer.publishCanceled(evt);
        log.info("Canceled ticket id={} orderId={} reason={} stage={}", t.getId(), t.getOrderId(), reason, stage);
    }

    private boolean isOpenNow() {
        var now = LocalTime.now();
        return !now.isBefore(open) && !now.isAfter(close);
    }

    private boolean hasCapacity() {
        var inProg = repo.findByStatus(TicketStatus.IN_PROGRESS).size();
        return inProg < maxConcurrentInProgress;
    }

    private Instant computeEta() {
        var queued = repo.findByStatus(TicketStatus.QUEUED).size();
        var minutes = baseMinutes + queued * perTicketQueueMinutes;
        return Instant.now().plusSeconds(minutes * 60L);
    }

    private void ensureTransition(TicketStatus from, TicketStatus to) {
        switch (to) {
            case IN_PROGRESS -> {
                if (!(from == TicketStatus.QUEUED || from == TicketStatus.READY)) {
                    throw new IllegalStateException("Cannot start from " + from);
                }
            }
            case READY -> {
                if (from != TicketStatus.IN_PROGRESS) throw new IllegalStateException("Must be IN_PROGRESS to mark READY");
            }
            case HANDED_OVER -> {
                if (from != TicketStatus.READY) throw new IllegalStateException("Must be READY to hand over");
            }
            case CANCELED -> {} // handled elsewhere
            default -> {}
        }
    }

    private String currentStage(KitchenTicket t) {
        return switch (t.getStatus()) {
            case QUEUED -> "PENDING";
            case IN_PROGRESS -> "IN_PROGRESS";
            case READY -> "READY";
            case HANDED_OVER -> "HANDED_OVER";
            case CANCELED -> "CANCELED";
        };
    }
}
