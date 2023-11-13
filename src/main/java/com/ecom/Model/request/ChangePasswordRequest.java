package com.ecom.Model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    private String email;

    @NotBlank(message = "Mật khẩu cũ trống")
    @Size(min = 6, max = 20, message = "Mật khẩu phải chứa từ 6 - 20 ký tự")
    @JsonProperty("old_password")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới trống")
    @Size(min = 6, max = 20, message = "Mật khẩu phải chứa từ 6 - 20 ký tự")
    @JsonProperty("new_password")
    private String newPassword;
}
