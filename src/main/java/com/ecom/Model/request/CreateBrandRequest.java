package com.ecom.Model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateBrandRequest {
    @NotBlank(message = "Tên nhãn hiệu trống!")
    @Size(max = 50,message = "Tên nhãn hiệu có độ dài tối đa 50 ký tự!")
    private String name;
    private Long id;
    private String description;
    private String thumbnail;
    private boolean status;
}
