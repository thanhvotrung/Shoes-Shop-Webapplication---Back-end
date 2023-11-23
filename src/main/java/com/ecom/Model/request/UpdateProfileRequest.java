package com.ecom.Model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    private String email;

    @Pattern(regexp = "(09|03|07|08|05)+([0-9]{8})\\b", message = "Điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Họ tên trống")
    @JsonProperty("full_name")
    private String fullName;

    private String address;
}
