package com.ecom.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "product_image")
public class ProductImage {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "product_id")
    private String productId;
    @Column(name = "image_url")
    private String imageUrl;
}
