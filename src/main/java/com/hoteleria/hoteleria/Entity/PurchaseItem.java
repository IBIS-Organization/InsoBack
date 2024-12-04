package com.hoteleria.hoteleria.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "purchase_items")
public class PurchaseItem {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private Float price;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "reserva_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_purchase_item_reserva"))
    private Reserva reserva;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "id"
            , foreignKey = @ForeignKey(name = "FK_purchase_item_purchase"))
    public Purchase purchase;
}