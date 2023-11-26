package com.ecom.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderInfoDTO {
    private Long id;
    private Long totalPrice;
    private String productImg;
    private String name;
    private int size;
    private int quantity;
    private Long price;
    private Long count;

}