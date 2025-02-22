package com.is.back.controllers;

import com.is.back.dto.AdminRequestDTO;
import com.is.back.services.AdminRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminRequestService adminRequestService;

    /**
     * Получить все заявки на регистрацию администраторов.
     *
     * @return Список заявок.
     */
    @GetMapping("/requests")
    public ResponseEntity<List<AdminRequestDTO>> getAllAdminRequests() {
        List<AdminRequestDTO> requests = adminRequestService.getAllAdminRequests();
        return ResponseEntity.ok(requests);
    }

    /**
     * Одобрить заявку на регистрацию администратора.
     *
     * @param requestId ID заявки.
     * @return Сообщение об успешном одобрении.
     */
    @PostMapping("/requests/{requestId}/approve")
    public ResponseEntity<String> approveAdminRequest(@PathVariable Long requestId) {
        AdminRequestDTO message = adminRequestService.approveAdminRequest(requestId);
        return ResponseEntity.ok(message.toString());
    }
}
