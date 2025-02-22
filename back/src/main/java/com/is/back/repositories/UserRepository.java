package com.is.back.repositories;

import com.is.back.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    // Найти пользователя по имени
    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    Optional<Users> findByUsername(@Param("username") String username);

    // Проверить, существует ли пользователь с заданным именем
    @Query(value = "SELECT COUNT(*) > 0 FROM users WHERE username = :username", nativeQuery = true)
    boolean existsByUsername(@Param("username") String username);
}
