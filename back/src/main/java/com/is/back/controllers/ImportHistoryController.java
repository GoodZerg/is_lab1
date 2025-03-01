package com.is.back.controllers;

import com.is.back.dto.ImportHistoryDTO;
import com.is.back.entity.ImportHistory;
import com.is.back.services.ImportHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/import-history")
public class ImportHistoryController {

    @Autowired
    private ImportHistoryService importHistoryService;

    @GetMapping("/user")
    public ResponseEntity<List<ImportHistoryDTO>> getHistoryForUser(@RequestParam Long userId) {
        List<ImportHistoryDTO> history = importHistoryService.getHistoryForUser(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ImportHistoryDTO>> getAllHistory() {
        List<ImportHistoryDTO> history = importHistoryService.getAllHistory();
        return ResponseEntity.ok(history);
    }
}
