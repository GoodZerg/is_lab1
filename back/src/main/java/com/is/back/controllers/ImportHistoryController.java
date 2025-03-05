package com.is.back.controllers;

import com.is.back.dto.ImportHistoryDTO;
import com.is.back.entity.ImportHistory;
import com.is.back.services.ImportHistoryService;
import com.is.back.services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/import-history")
public class ImportHistoryController {

    @Autowired
    private ImportHistoryService importHistoryService;

    @Autowired
    private MinioService minioService;
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

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws Exception {
        InputStreamResource resource = new InputStreamResource(
                minioService.downloadFile(fileName)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
