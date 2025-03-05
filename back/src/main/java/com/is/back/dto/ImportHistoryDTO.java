package com.is.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImportHistoryDTO {
    private Long userId; // ID пользователя, запустившего операцию
    private LocalDateTime timestamp; // Время запуска операции
    private String fileName; // Имя файла в бакете
    private String status; // Статус операции (SUCCESS, FAILED)
    private Integer addedObjects;
}
