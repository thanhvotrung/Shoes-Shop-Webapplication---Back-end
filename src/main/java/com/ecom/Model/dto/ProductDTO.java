package com.ecom.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDTO {
    private String id;
    private String name;
    private  String description;
    private long price;
    private long salePrice;
    private long totalSold;
    private int status;
    private ArrayList<String> images;
    private ArrayList<String> feedBackImages;
}