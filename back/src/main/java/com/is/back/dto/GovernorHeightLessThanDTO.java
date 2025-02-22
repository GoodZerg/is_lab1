package com.is.back.dto;

import lombok.Data;

import java.util.List;

@Data

public class GovernorHeightLessThanDTO {
    private List<CityDTO> cities;
}