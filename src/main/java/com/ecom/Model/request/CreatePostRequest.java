package com.ecom.Model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CreatePostRequest {
    @NotNull(message = "Tiêu đề rỗng")
    @NotEmpty(message = "Tiêu đề rỗng")
    @Size(min = 1, max = 300, message = "Độ dài tiêu đề từ 1 - 300 ký tự")
    private String title;

    @NotNull(message = "Nội dung rỗng")
    @NotEmpty(message = "Nội dung rỗng")
    private String content;

    private String description;

    private int status;

    private String image;
    private String email;
}
