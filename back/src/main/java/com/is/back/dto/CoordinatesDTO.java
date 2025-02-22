package com.is.back.dto;

import lombok.Data;

@Data

public class CoordinatesDTO {
    private float x;
    private float y;

    public CoordinatesDTO(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
