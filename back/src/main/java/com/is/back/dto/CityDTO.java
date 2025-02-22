package com.is.back.dto;

import com.is.back.entity.Climate;
import com.is.back.entity.Government;
import com.is.back.entity.StandardOfLiving;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data

public class CityDTO {
    private Long id;
    private String name;
    private CoordinatesDTO coordinates;
    private Date creationDate;
    private Integer area;
    private Integer population;
    private LocalDateTime establishmentDate;
    private boolean capital;
    private long metersAboveSeaLevel;
    private Climate climate;
    private Government government;
    private StandardOfLiving standardOfLiving;
    private HumanDTO governor;
    private Long userId; // ID пользователя, создавшего город
}
