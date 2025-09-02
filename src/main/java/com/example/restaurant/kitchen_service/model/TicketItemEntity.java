package com.example.restaurant.kitchen_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_item", indexes = {
        @Index(name = "ix_item_ticket", columnList = "ticket_id")
})
public class TicketItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private KitchenTicket ticket;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private int qty;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KitchenTicket getTicket() {
        return ticket;
    }

    public void setTicket(KitchenTicket ticket) {
        this.ticket = ticket;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
