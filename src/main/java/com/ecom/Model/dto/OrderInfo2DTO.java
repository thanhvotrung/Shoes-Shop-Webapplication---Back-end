package com.ecom.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo2DTO {
    private long id;
    private long totalPrice;
    List<ProductOrderInfo> productOrderInfos;
    private int count;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ProductOrderInfo {
        private String productImg;
        private String name;
        private int size;
        private int quantity;
        private long price;
    }

}