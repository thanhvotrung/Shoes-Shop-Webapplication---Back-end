package com.ecom.Model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductInfoDTO {
    private String id;
    private String name;
    private String slug;
    private long price;
    private int views;
    private String images;
    private int totalSold;
    private long promotionPrice;

    public ProductInfoDTO(String id, String name, String slug, long price, int views, String images, int totalSold) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.views = views;
        this.images = images;
        this.totalSold = totalSold;
    }
}