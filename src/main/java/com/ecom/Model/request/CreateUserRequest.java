package com.ecom.Model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateUserRequest {

    @NotBlank(message = "Họ tên trống")
    private String fullName;

    @NotBlank(message = "Email trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Mật khẩu trống")
    @Size(min = 6,max = 20, message = "Mật khẩu phải chứa từ 6-20 ký tự")
    private String password;

    @Pattern(regexp="(84|0[3|5|7|8|9])+([0-9]{8})\\b",message = "Số điện thoại không hợp lệ!")
    private String phone;

    private String address;
}

