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
public class CreateCategoryRequest {
    @NotBlank(message = "Tên danh mục trống")
    @Size(max = 50,message = "Tên danh mục có độ dài tối đa 50 ký tự!")
    private String name;

    private boolean status;
}
