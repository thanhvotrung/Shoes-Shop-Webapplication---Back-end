package com.ecom.Model.dto;

import com.ecom.Entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private long id;
    private long subtotalPrice;
    private long totalPrice;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String productImg;
    private String name;
    private int size;
    private int quantity;
    private long price;
    private int status;
    private String statusText;
    private String note;

    public OrderDetailDTO(long id, long subtotalPrice, long totalPrice, String receiverName, String receiverPhone, String receiverAddress, String productImg, String name, int size, int quantity, long price, int status, String note) {
        this.id = id;
        this.subtotalPrice = subtotalPrice;
//        this.promotion = promotion;
        this.totalPrice = totalPrice;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.productImg = productImg;
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.note = note;
    }
}
