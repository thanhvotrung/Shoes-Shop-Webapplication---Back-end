package com.ecom.Model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateOrderDetailsRequest {
    @NotBlank(message = "Sản phẩm trống")
    @JsonProperty("product_id")
    private String productId;
    @Min(value = 35)
    @Max(value = 42)
    private int size;
    private int quantity;
    @JsonProperty("product_price")
    private long productPrice;

}
