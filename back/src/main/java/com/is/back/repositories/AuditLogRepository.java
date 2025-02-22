package com.is.back.repositories;

import com.is.back.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Найти все логи изменений для определенного города
    @Query(value = "SELECT * FROM audit_log WHERE city_id = :cityId", nativeQuery = true)
    List<AuditLog> findByCityId(@Param("cityId") Long cityId);

    // Найти все логи изменений, выполненные определенным пользователем
    @Query(value = "SELECT * FROM audit_log WHERE user_id = :userId", nativeQuery = true)
    List<AuditLog> findByUserId(@Param("userId") Long userId);
}
