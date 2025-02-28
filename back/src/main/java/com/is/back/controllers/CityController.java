package com.is.back.controllers;

import com.is.back.dto.CityDTO;
import com.is.back.dto.CoordinatesDTO;
import com.is.back.dto.HumanDTO;
import com.is.back.dto.MessageDTO;
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

    @GetMapping("/coordinates")
    public ResponseEntity<List<CoordinatesDTO>> getAllCoordinates() {
        List<CoordinatesDTO> coordinates = cityService.getAllCoordinates();
        return ResponseEntity.ok(coordinates);
    }

    @GetMapping("/governors")
    public ResponseEntity<List<HumanDTO>> getAllGovernors() {
        List<HumanDTO> governors = cityService.getAllHumans();
        return ResponseEntity.ok(governors);
    }

    /**
     * Получить город по ID.
     *
     * @param dto ID города.
     * @return Город.
     */
    @PostMapping("/get")
    public ResponseEntity<CityDTO> getCityById(@RequestBody MessageDTO dto) {
        CityDTO city = cityService.getCityById(Long.valueOf(dto.getMessage()));
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
     * @param cityDTO Обновленные данные города.
     * @return Обновленный город.
     */
    @PostMapping("/update")
    public ResponseEntity<CityDTO> updateCity(@RequestBody CityDTO cityDTO) {
        CityDTO updatedCity = cityService.updateCity(cityDTO.getId(), cityDTO);
        return ResponseEntity.ok(updatedCity);
    }

    /**
     * Удалить город.
     *
     * @param dto ID города.
     * @return Сообщение об успешном удалении.
     */
    @PostMapping("/delete")
    public ResponseEntity<MessageDTO> deleteCity(@RequestBody MessageDTO dto) {
        cityService.deleteCity(Long.valueOf(dto.getMessage()));
        return ResponseEntity.ok(new MessageDTO("City deleted successfully"));
    }
}
