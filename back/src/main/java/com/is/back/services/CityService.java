package com.is.back.services;

import com.is.back.dto.*;
import com.is.back.entity.*;
import com.is.back.exception.NotFoundException;
import com.is.back.repositories.CityRepository;
import com.is.back.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    // Получить все города
    @Transactional(readOnly = true)
    public List<CityDTO> getAllCities() {
        return cityRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Получить город по ID
    @Transactional(readOnly = true)
    public CityDTO getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("City not found with id: " + id));
        return convertToDTO(city);
    }

    // Создать новый город
    @Transactional
    public CityDTO createCity(CityDTO cityDTO) {
        Users user = userRepository.findById(cityDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + cityDTO.getUserId()));

        City city = convertToEntity(cityDTO);
        city.setUser(user);
        City savedCity = cityRepository.save(city);
        return convertToDTO(savedCity);
    }

    // Обновить город
    @Transactional
    public CityDTO updateCity(Long id, CityDTO cityDTO) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("City not found with id: " + id));

        existingCity.setName(cityDTO.getName());
        existingCity.setCoordinates(new Coordinates(cityDTO.getCoordinates().getX(), cityDTO.getCoordinates().getY()));
        existingCity.setArea(cityDTO.getArea());
        existingCity.setPopulation(cityDTO.getPopulation());
        existingCity.setEstablishmentDate(cityDTO.getEstablishmentDate());
        existingCity.setCapital(cityDTO.isCapital());
        existingCity.setMetersAboveSeaLevel(cityDTO.getMetersAboveSeaLevel());
        existingCity.setClimate(cityDTO.getClimate());
        existingCity.setGovernment(cityDTO.getGovernment());
        existingCity.setStandardOfLiving(cityDTO.getStandardOfLiving());
        existingCity.setGovernor(new Human(cityDTO.getGovernor().getHeight()));

        City updatedCity = cityRepository.save(existingCity);
        return convertToDTO(updatedCity);
    }

    // Удалить город
    @Transactional
    public void deleteCity(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new NotFoundException("City not found with id: " + id);
        }
        cityRepository.deleteById(id);
    }

    // Преобразовать сущность City в CityDTO
    private CityDTO convertToDTO(City city) {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(city.getId());
        cityDTO.setName(city.getName());
        cityDTO.setCoordinates(new CoordinatesDTO(city.getCoordinates().getX(), city.getCoordinates().getY()));
        cityDTO.setCreationDate(city.getCreationDate());
        cityDTO.setArea(city.getArea());
        cityDTO.setPopulation(city.getPopulation());
        cityDTO.setEstablishmentDate(city.getEstablishmentDate());
        cityDTO.setCapital(city.isCapital());
        cityDTO.setMetersAboveSeaLevel(city.getMetersAboveSeaLevel());
        cityDTO.setClimate(city.getClimate());
        cityDTO.setGovernment(city.getGovernment());
        cityDTO.setStandardOfLiving(city.getStandardOfLiving());
        cityDTO.setGovernor(new HumanDTO(city.getGovernor().getHeight()));
        cityDTO.setUserId(city.getUser().getId());
        return cityDTO;
    }

    // Преобразовать CityDTO в сущность City
    private City convertToEntity(CityDTO cityDTO) {
        City city = new City();
        city.setName(cityDTO.getName());
        city.setCoordinates(new Coordinates(cityDTO.getCoordinates().getX(), cityDTO.getCoordinates().getY()));
        city.setArea(cityDTO.getArea());
        city.setPopulation(cityDTO.getPopulation());
        city.setEstablishmentDate(cityDTO.getEstablishmentDate());
        city.setCapital(cityDTO.isCapital());
        city.setMetersAboveSeaLevel(cityDTO.getMetersAboveSeaLevel());
        city.setClimate(cityDTO.getClimate());
        city.setGovernment(cityDTO.getGovernment());
        city.setStandardOfLiving(cityDTO.getStandardOfLiving());
        city.setGovernor(new Human(cityDTO.getGovernor().getHeight()));
        return city;
    }
}