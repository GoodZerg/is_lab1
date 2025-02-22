package com.is.back.controllers;

import com.is.back.dto.CityDTO;
import com.is.back.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityController {
    private final CityService cityService;

    /**
     * Получить все города.
     *
     * @return Список городов.
     */
    @GetMapping("/list")
    public ResponseEntity<List<CityDTO>> getAllCities() {
        List<CityDTO> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    /**
     * Получить город по ID.
     *
     * @param id ID города.
     * @return Город.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCityById(@PathVariable Long id) {
        CityDTO city = cityService.getCityById(id);
        return ResponseEntity.ok(city);
    }

    /**
     * Создать новый город.
     *
     * @param cityDTO Данные города.
     * @return Созданный город.
     */
    @PostMapping("/create")
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO) {
        CityDTO createdCity = cityService.createCity(cityDTO);
        return ResponseEntity.ok(createdCity);
    }

    /**
     * Обновить город.
     *
     * @param id      ID города.
     * @param cityDTO Обновленные данные города.
     * @return Обновленный город.
     */
    @PostMapping("/{id}/update")
    public ResponseEntity<CityDTO> updateCity(@PathVariable Long id, @RequestBody CityDTO cityDTO) {
        CityDTO updatedCity = cityService.updateCity(id, cityDTO);
        return ResponseEntity.ok(updatedCity);
    }

    /**
     * Удалить город.
     *
     * @param id ID города.
     * @return Сообщение об успешном удалении.
     */
    @PostMapping("/{id}/delete")
    public ResponseEntity<String> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.ok("City deleted successfully");
    }
}
