package com.is.back.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.is.back.dto.*;
import com.is.back.entity.*;
import com.is.back.exception.NotFoundException;
import com.is.back.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.datatype.jsr310.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private AuditLogRepository audRepository;
    @Autowired
    private HumanRepository humanRepository;
    @Autowired
    private CoordinatesRepository coordinatesRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private ImportHistoryService importHistoryService;

    // Получить все города
    @Transactional(readOnly = true)
    public List<CityDTO> getAllCities() {
        return cityRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HumanDTO> getAllHumans() {
        return humanRepository.findAll().stream()
                .map(this::convertHToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CoordinatesDTO> getAllCoordinates() {
        return coordinatesRepository.findAll().stream()
                .map(this::convertCToDTO)
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CityDTO createCity(CityDTO cityDTO) throws Exception {
        Users user = userRepository.findById(cityDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + cityDTO.getUserId()));

        City city = convertToEntity(cityDTO);
        city.setUser(user);

        checkUniqueness(city.getName(), city.getId());

        City savedCity = cityRepository.save(city);

        auditLogService.saveAuditLog(user, savedCity, "Create city");
        return convertToDTO(savedCity);
    }

    // Обновить город
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CityDTO updateCity(Long id, CityDTO cityDTO) throws Exception {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("City not found with id: " + id));

        try {
            checkUniqueness(cityDTO.getName(), id);
            moveDTOtoEntity(cityDTO, existingCity);
        } catch (Exception e) {
            //e.printStackTrace();
            throw  new Exception("City name shall be UNIQUE");
        }

        City updatedCity = cityRepository.save(existingCity);

        Users userUpdate = userRepository.findById(cityDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + cityDTO.getUserId()));

        auditLogService.saveAuditLog(userUpdate, updatedCity, "Update city");

        return convertToDTO(updatedCity);
    }

    // Удалить город
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteCity(Long id) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("City not found with id: " + id));

        List<City> citiesToDelete = new ArrayList<>();
        citiesToDelete.add(existingCity);

        Long coordId = existingCity.getCoordinates().getId();
        Long govrId = existingCity.getGovernor().getId();

        List<City> allCities = cityRepository.findAll();

        for (City city : allCities) {
            if (city.getCoordinates().getId().equals(coordId) || city.getGovernor().getId().equals(govrId)) {
                citiesToDelete.add(city);
            }
        }

        /*if (!cityRepository.existsById(id)) {
            throw new NotFoundException("City not found with id: " + id);
        }*/
        //cityRepository.deleteById(id);
        cityRepository.deleteAll(citiesToDelete);
        coordinatesRepository.deleteById(coordId);
        humanRepository.deleteById(govrId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int importCitiesFromJson(MultipartFile file, Long userId) {
        try {
            System.out.println("File content: " + new String(file.getBytes()));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            List<City> cities = objectMapper.readValue(file.getInputStream(), new TypeReference<List<City>>() {});

            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

            List<Coordinates> coords = new java.util.ArrayList<>(Collections.emptyList());
            List<Human> humans = new java.util.ArrayList<>(Collections.emptyList());

            // Проверка данных перед сохранением
            int index = 0;
            for (City city : cities) {
                index++;
                checkUniqueness(city.getName(), city.getId());
                checkUniquenessImport(city, cities.subList(index, cities.size()));
                city.setUser(user);
                city.setCreationDate(new Date());
                validateCity(city);

                coords.add(city.getCoordinates());
                humans.add(city.getGovernor());
            }

            // Сохранение всех городов в одной транзакции
            coordinatesRepository.saveAll(coords);
            humanRepository.saveAll(humans);
            cityRepository.saveAll(cities);

            return cities.size();
            //_saveImportHistory(userId, "SUCCESS", cities.size());
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("Error during import: " + e.getMessage(), e);
        }
    }

    private void validateCity(City city) throws Exception {
        if (city.getName() == null || city.getName().isEmpty()) {
            throw new Exception("City name is required.");
        }
        if (city.getCoordinates() == null ) {
            throw new Exception("Coordinates are required.");
        }
        if (city.getArea() < 0) {
            throw new Exception("Area must be greater than 0.");
        }
        if (city.getPopulation() < 0) {
            throw new Exception("Population cannot be negative.");
        }
        if (city.getGovernor() == null || city.getGovernor().getHeight() < 0) {
            throw new Exception("Governor height must be greater than 0.");
        }
    }

    private void checkUniquenessImport(City city, List<City> cityList) throws Exception {
        for (City city1 : cityList) {
            if (city1.getName().equals(city.getName())) {
                throw new Exception("City name shall be UNIQUE");
            }
        }
    }

    private void checkUniqueness(String name, Long id) throws Exception {
        List<City> sameNameCities =  cityRepository.findByName(name);
        if (sameNameCities != null && !sameNameCities.isEmpty()) {
            for (City city1 : sameNameCities) {
                if (!city1.getId().equals(id)) {
                    throw new Exception("City name shall be UNIQUE");
                }
            }
        }
    }

    // Преобразовать сущность City в CityDTO
    private CityDTO convertToDTO(City city) {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(city.getId());
        cityDTO.setName(city.getName());
        cityDTO.setCoordinates(convertCToDTO(city.getCoordinates()));
        cityDTO.setCreationDate(city.getCreationDate());
        cityDTO.setArea(city.getArea());
        cityDTO.setPopulation(city.getPopulation());
        cityDTO.setEstablishmentDate(city.getEstablishmentDate());
        cityDTO.setCapital(city.isCapital());
        cityDTO.setMetersAboveSeaLevel(city.getMetersAboveSeaLevel());
        cityDTO.setClimate(city.getClimate());
        cityDTO.setGovernment(city.getGovernment());
        cityDTO.setStandardOfLiving(city.getStandardOfLiving());
        cityDTO.setGovernor(convertHToDTO(city.getGovernor()));
        cityDTO.setUserId(city.getUser().getId());
        return cityDTO;
    }

    private CoordinatesDTO convertCToDTO(Coordinates coordinates) {
        return new CoordinatesDTO(coordinates.getId(), coordinates.getX(), coordinates.getY());
    }

    private HumanDTO convertHToDTO(Human human) {
        return new HumanDTO(human.getId(), human.getHeight());
    }


    private void moveDTOtoEntity(CityDTO cityDTO, City city) {
        city.setName(cityDTO.getName());
        city.setCreationDate(cityDTO.getCreationDate());
        city.setCoordinates(convertCToEntity(cityDTO.getCoordinates()));
        //city.setCoordinates(new Coordinates(cityDTO.getCoordinates().getX(), cityDTO.getCoordinates().getY()));
        city.setArea(cityDTO.getArea());
        city.setPopulation(cityDTO.getPopulation());
        city.setEstablishmentDate(cityDTO.getEstablishmentDate());
        city.setCapital(cityDTO.isCapital());
        city.setMetersAboveSeaLevel(cityDTO.getMetersAboveSeaLevel());
        city.setClimate(cityDTO.getClimate());
        city.setGovernment(cityDTO.getGovernment());
        city.setStandardOfLiving(cityDTO.getStandardOfLiving());
        city.setGovernor(convertHToEntity(cityDTO.getGovernor()));
        //city.setGovernor(new Human(cityDTO.getGovernor().getHeight()));
    }

    // Преобразовать CityDTO в сущность City
    private City convertToEntity(CityDTO cityDTO) {
        City city = new City();
        moveDTOtoEntity(cityDTO, city);
        return city;
    }

    private Coordinates convertCToEntity(CoordinatesDTO coordinatesDTO) {
         return
                 coordinatesRepository.findById(coordinatesDTO.getId())
                         .orElseGet(
                                 () -> coordinatesRepository.save(
                                         new Coordinates(
                                            coordinatesDTO.getX(),
                                            coordinatesDTO.getY()
                                        )
                                 )
                         );
    }

    private Human convertHToEntity(HumanDTO humanDTO) {
        return
                humanRepository.findById(humanDTO.getId())
                        .orElseGet(
                                () -> humanRepository.save(
                                        new Human(
                                                humanDTO.getHeight()
                                        )
                                )
                        );
    }
}