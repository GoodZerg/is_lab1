package com.is.back.repositories;

import com.is.back.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    // Найти все города по имени (с учетом регистра)
    @Query(value = "SELECT * FROM city WHERE name = :name", nativeQuery = true)
    List<City> findByName(@Param("name") String name);

    // Найти все города, имя которых начинается с заданной подстроки
    @Query(value = "SELECT * FROM city WHERE name LIKE :prefix%", nativeQuery = true)
    List<City> findByNameStartingWith(@Param("prefix") String prefix);

    @Query(value = "SELECT city.* FROM city JOIN human h ON governor_id = h.id WHERE h.height < :max_height", nativeQuery = true)
    List<City> findByGovernorLessThan(@Param("max_height") long max_height);
    // Найти все города, где значение поля metersAboveSeaLevel больше заданного
    @Query(value = "SELECT * FROM city WHERE meters_above_sea_level > :meters", nativeQuery = true)
    List<City> findByMetersAboveSeaLevelGreaterThan(@Param("meters") long meters);

    // Найти все города, созданные определенным пользователем
    @Query(value = "SELECT * FROM city WHERE user_id = :userId", nativeQuery = true)
    List<City> findByUserId(@Param("userId") Long userId);

    // Найти все столицы
    @Query(value = "SELECT * FROM city WHERE capital = true", nativeQuery = true)
    List<City> findByCapitalTrue();

    // Найти города с наименьшим населением
    @Query(value = "SELECT * FROM city ORDER BY population ASC LIMIT 3", nativeQuery = true)
    List<City> findTop3ByOrderByPopulationAsc();
}