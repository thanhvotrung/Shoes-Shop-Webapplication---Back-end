package com.ecom.Model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateOrdersRequest {
    private String email;
    @NotBlank(message = "Họ tên trống")
    @JsonProperty("receiver_name")
    private String receiverName;
    @Pattern(regexp = "(09|03|07|08|05)+([0-9]{8})\\b", message = "Điện thoại không hợp lệ")
    @JsonProperty("receiver_phone")
    private String receiverPhone;
    @NotNull(message = "Địa chỉ trống")
    @NotEmpty(message = "Địa chỉ trống")
    @JsonProperty("receiver_address")
    private String receiverAddress;
    @JsonProperty("coupon_code")
    private String couponCode;
    @JsonProperty("total_price")
    private long totalPrice;
    @JsonProperty("subtotal_price")
    private long subtotalPrice;
    private String note;
    private List<CreateOrderDetailsRequest> products;
}
