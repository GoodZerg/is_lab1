package com.is.back.services;

import com.is.back.dto.AuditLogDTO;
import com.is.back.entity.AuditLog;
import com.is.back.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    // Получить все логи
    @Transactional(readOnly = true)
    public List<AuditLogDTO> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Преобразовать сущность AuditLog в AuditLogDTO
    private AuditLogDTO convertToDTO(AuditLog auditLog) {
        AuditLogDTO auditLogDTO = new AuditLogDTO();
        auditLogDTO.setId(auditLog.getId());
        auditLogDTO.setCityId(auditLog.getCity().getId());
        auditLogDTO.setUserId(auditLog.getUser().getId());
        auditLogDTO.setAction(auditLog.getAction());
        auditLogDTO.setActionDate(auditLog.getActionDate());
        return auditLogDTO;
    }
}