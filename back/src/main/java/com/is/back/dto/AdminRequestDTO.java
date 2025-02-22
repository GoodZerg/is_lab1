package com.is.back.dto;

import lombok.Data;

import java.util.Date;

@Data

public class AdminRequestDTO {
    private Long id;
    private Long userId;
    private String status;
    private Date requestDate;
}
