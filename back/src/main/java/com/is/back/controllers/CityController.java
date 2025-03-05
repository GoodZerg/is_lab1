package com.is.back.controllers;

import com.is.back.dto.*;
import com.is.back.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.datatype.jsr310.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityController {
    private final CityService cityService;

    private final WebSocketController webSocketController;
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
    public ResponseEntity<?> createCity(@RequestBody CityDTO cityDTO) {
        try {
            CityDTO createdCity = cityService.createCity(cityDTO);

            webSocketController.sendCitiesUpdate();

            return ResponseEntity.ok(createdCity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO(e.getMessage()));
        }
    }

    /**
     * Обновить город.
     *
     * @param cityDTO Обновленные данные города.
     * @return Обновленный город.
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateCity(@RequestBody CityDTO cityDTO) {
        try {
            CityDTO updatedCity = cityService.updateCity(cityDTO.getId(), cityDTO);

            webSocketController.sendCitiesUpdate();

            return ResponseEntity.ok(updatedCity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO(e.getMessage()));
        }
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDTO> importCities(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId ) {
        try {
            cityService.importCitiesFromJson(file, userId);

            webSocketController.sendCitiesUpdate();

            return ResponseEntity.ok(new MessageDTO("Cities imported correctly"));
        } catch (Exception e) {
            cityService._saveImportHistory(userId, "FAIL", 0);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO(e.getMessage()));
        }
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

        webSocketController.sendCitiesUpdate();

        return ResponseEntity.ok(new MessageDTO("City deleted successfully"));
    }
}
