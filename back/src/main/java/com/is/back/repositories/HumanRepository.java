package com.is.back.repositories;

import com.is.back.entity.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HumanRepository extends JpaRepository<Human, Long> {

    // Найти всех людей с ростом меньше заданного
    @Query(value = "SELECT * FROM human WHERE height < :height", nativeQuery = true)
    List<Human> findByHeightLessThan(@Param("height") Double height);
}
