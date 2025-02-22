package com.is.back.dto;

import lombok.Data;

import java.util.Date;

@Data

public class AuditLogDTO {
    private Long id;
    private Long cityId;
    private Long userId;
    private String action;
    private Date actionDate;
}
