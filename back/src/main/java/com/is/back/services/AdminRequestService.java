package com.is.back.services;


import com.is.back.dto.AdminRequestDTO;
import com.is.back.entity.AdminRequest;
import com.is.back.entity.Users;
import com.is.back.exception.NotFoundException;
import com.is.back.repositories.AdminRequestRepository;
import com.is.back.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminRequestService {
    private final AdminRequestRepository adminRequestRepository;
    private final UserRepository userRepository;

    // Получить все заявки
    @Transactional(readOnly = true)
    public List<AdminRequestDTO> getAllAdminRequests() {
        return adminRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Создать новую заявку
    @Transactional
    public AdminRequestDTO createAdminRequest(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        AdminRequest adminRequest = new AdminRequest();
        adminRequest.setUser(user);
        adminRequest.setStatus("PENDING");
        adminRequest.setRequestDate(new Date());

        AdminRequest savedRequest = adminRequestRepository.save(adminRequest);
        return convertToDTO(savedRequest);
    }

    // Одобрить заявку
    @Transactional
    public AdminRequestDTO approveAdminRequest(Long requestId) {
        AdminRequest adminRequest = adminRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Admin request not found with id: " + requestId));

        adminRequest.setStatus("APPROVED");

        Users user = userRepository.findById(adminRequest.getUser().getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + adminRequest.getUser().getId()));
        user.setRole("ADMIN");
        userRepository.save(user);

        AdminRequest updatedRequest = adminRequestRepository.save(adminRequest);
        return convertToDTO(updatedRequest);
    }

    // Преобразовать сущность AdminRequest в AdminRequestDTO
    private AdminRequestDTO convertToDTO(AdminRequest adminRequest) {
        AdminRequestDTO adminRequestDTO = new AdminRequestDTO();
        adminRequestDTO.setId(adminRequest.getId());
        adminRequestDTO.setUserId(adminRequest.getUser().getId());
        adminRequestDTO.setStatus(adminRequest.getStatus());
        adminRequestDTO.setRequestDate(adminRequest.getRequestDate());
        return adminRequestDTO;
    }
}