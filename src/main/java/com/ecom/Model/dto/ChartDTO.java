package com.ecom.Model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChartDTO {
    private String label;
    private int value;

    public ChartDTO(String label, int value) {
        this.label = label;
        this.value = value;
    }
}

