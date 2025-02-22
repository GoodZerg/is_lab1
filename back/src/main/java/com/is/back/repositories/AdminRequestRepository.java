package com.is.back.repositories;

import com.is.back.entity.AdminRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRequestRepository extends JpaRepository<AdminRequest, Long> {

    // Найти все заявки на регистрацию администратора с определенным статусом
    @Query(value = "SELECT * FROM admin_request WHERE status = :status", nativeQuery = true)
    List<AdminRequest> findByStatus(@Param("status") String status);

    // Найти все заявки, созданные определенным пользователем
    @Query(value = "SELECT * FROM admin_request WHERE user_id = :userId", nativeQuery = true)
    List<AdminRequest> findByUserId(@Param("userId") Long userId);
}