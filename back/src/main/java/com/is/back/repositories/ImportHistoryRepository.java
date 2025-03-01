package com.is.back.repositories;

import com.is.back.entity.ImportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImportHistoryRepository extends JpaRepository<ImportHistory, Long> {

    @Query(value = "SELECT * FROM import_history WHERE import_history.user_id = :userId", nativeQuery = true)
    List<ImportHistory> findByUserId(@Param("userId") Long userId);

}