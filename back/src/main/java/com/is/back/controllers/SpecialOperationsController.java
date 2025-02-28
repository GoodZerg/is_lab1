package com.is.back.controllers;


import com.is.back.dto.AverageMetersAboveSeaLevelDTO;
import com.is.back.dto.CityDTO;
import com.is.back.dto.MessageDTO;
import com.is.back.dto.RelocatePopulationDTO;
import com.is.back.services.SpecialOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/special-operations")
public class SpecialOperationsController {
    private final SpecialOperationsService specialOperationsService;

    /**
     * Рассчитывает среднее значение поля metersAboveSeaLevel для всех городов.
     *
     * @return Среднее значение.
     */
    @GetMapping("/average-meters-above-sea-level")
    public ResponseEntity<AverageMetersAboveSeaLevelDTO> calculateAverageMetersAboveSeaLevel() {
        AverageMetersAboveSeaLevelDTO result = specialOperationsService.calculateAverageMetersAboveSeaLevel();
        return ResponseEntity.ok(result);
    }

    /**
     * Возвращает города, имя которых начинается с заданной подстроки.
     *
     * @param prefix Подстрока.
     * @return Список городов.
     */
    @GetMapping("/cities-name-starts-with")
    public ResponseEntity<List<CityDTO>> getCitiesByNameStartingWith(@RequestParam String prefix) {
        List<CityDTO> cities = specialOperationsService.getCitiesByNameStartingWith(prefix);
        return ResponseEntity.ok(cities);
    }

    /**
     * Возвращает города, у которых значение поля governor.height меньше заданного.
     *
     * @param height Максимальная высота губернатора.
     * @return Список городов.
     */
    @GetMapping("/cities-by-governor-height")
    public ResponseEntity<List<CityDTO>> getCitiesByGovernorHeightLessThan(@RequestParam Double height) {
        List<CityDTO> cities = specialOperationsService.getCitiesByGovernorHeightLessThan(height);
        return ResponseEntity.ok(cities);
    }

    /**
     * Переселяет всё население города с заданным ID в город с наименьшим населением.
     *
     * @param sourceCityId ID исходного города.
     * @return Сообщение об успешном выполнении.
     */
    @GetMapping("/relocate-all-population")
    public ResponseEntity<MessageDTO> relocateAllPopulation(@RequestParam Long sourceCityId) {
        String message = specialOperationsService.relocateAllPopulation(sourceCityId);
        return ResponseEntity.ok(new MessageDTO(message));
    }

    /**
     * Переселяет 50% жителей столицы в три города с наименьшим населением.
     *
     * @return Сообщение об успешном выполнении.
     */
    @GetMapping("/relocate-half-capital-population")
    public ResponseEntity<MessageDTO> relocateHalfOfCapitalPopulation() {
        String message = specialOperationsService.relocateHalfOfCapitalPopulation();
        return ResponseEntity.ok(new MessageDTO(message));
    }
}
