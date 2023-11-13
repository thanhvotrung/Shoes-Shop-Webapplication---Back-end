package com.ecom.Model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StatisticDTO {
    private long sales;
    private long profit;
    private int quantity;
    private String createdAt;

    public StatisticDTO(long sales, long profit, int quantity,String createdAt){
        this.sales = sales;
        this.profit = profit;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }
}
