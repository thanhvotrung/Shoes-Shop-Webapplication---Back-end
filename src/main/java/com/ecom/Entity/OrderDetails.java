package com.ecom.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "size")
    private int size;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private long price;
    @Column(name = "orders_id")
    private long ordersId;

}
