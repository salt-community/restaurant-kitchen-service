package com.example.restaurant.kitchen_service.model;


import com.example.restaurant.kitchen_service.enums.TicketStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "kitchen_ticket",
        indexes = {
                @Index(name = "ix_ticket_order", columnList = "order_id"),
                @Index(name = "ix_ticket_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_ticket_order", columnNames = "order_id")
        }
)
public class KitchenTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id", nullable = false, updatable = false)
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private TicketStatus status = TicketStatus.IN_PROGRESS;

    @Column(name = "estimated_ready_at")
    private Instant estimatedReadyAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;
//    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<TicketItemEntity> items = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "ticket_recipe", joinColumns = @JoinColumn(name = "ticket_id"), inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> recipes = new ArrayList<>();


    @PrePersist
    void prePersist() {
        var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }


//    public void addItem(TicketItemEntity item) {
//        item.setTicket(this);
//        items.add(item);
//    }
//
//    public void removeItem(TicketItemEntity item) {
//        items.remove(item);
//        item.setTicket(null);
//    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Instant getEstimatedReadyAt() {
        return estimatedReadyAt;
    }

    public void setEstimatedReadyAt(Instant estimatedReadyAt) {
        this.estimatedReadyAt = estimatedReadyAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

//    public List<TicketItemEntity> getItems() {
//        return items;
//    }
//
//    public void setItems(List<TicketItemEntity> items) {
//        this.items = items;
//    }
}
