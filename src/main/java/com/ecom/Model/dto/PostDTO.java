package com.ecom.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private long id;

    private String slug;

    private String title;

    private String thumbnail;

    private String createdAt;

    private String publishedAt;

    private String status;
}
