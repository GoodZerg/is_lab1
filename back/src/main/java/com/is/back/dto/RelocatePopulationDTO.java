package com.is.back.dto;

import lombok.Data;

@Data

public class RelocatePopulationDTO {
    private Long sourceCityId;
    private Long targetCityId;
    private int populationToRelocate;
}