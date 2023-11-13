package com.ecom.Model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateCommentPostRequest {
    private long postId;
    @NotBlank(message = "Nội dung trống!")
    private String content;
}
