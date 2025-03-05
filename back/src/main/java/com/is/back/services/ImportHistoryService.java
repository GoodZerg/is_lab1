package com.is.back.services;

import com.is.back.dto.ImportHistoryDTO;
import com.is.back.entity.ImportHistory;
import com.is.back.repositories.ImportHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImportHistoryService {

    @Autowired
    private ImportHistoryRepository importHistoryRepository;

    // Сохранить новую запись в истории
    public void saveImportHistory(ImportHistoryDTO importHistoryDTO) {
        ImportHistory history = new ImportHistory();

        history.setUserId(importHistoryDTO.getUserId());
        history.setTimestamp(LocalDateTime.now());
        history.setFileName(importHistoryDTO.getFileName());
        history.setStatus(importHistoryDTO.getStatus());
        history.setAddedObjects(importHistoryDTO.getAddedObjects());

        importHistoryRepository.save(history);
    }

    public void _saveImportHistory(Long userId, String fileName, String status, int addedObjects) {
        ImportHistoryDTO historyDTO = new ImportHistoryDTO();
        historyDTO.setUserId(userId);
        historyDTO.setFileName(fileName);
        historyDTO.setStatus(status);
        historyDTO.setAddedObjects(addedObjects);
        saveImportHistory(historyDTO);
    }

    // Получить историю для пользователя
    public List<ImportHistoryDTO> getHistoryForUser(Long userId) {
        return importHistoryRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Получить всю историю (для администратора)
    public List<ImportHistoryDTO> getAllHistory() {
        return importHistoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ImportHistoryDTO convertToDTO(ImportHistory importHistory) {
        return new ImportHistoryDTO(
                importHistory.getUserId(),
                importHistory.getTimestamp(),
                importHistory.getFileName(),
                importHistory.getStatus(),
                importHistory.getAddedObjects());
    }
}