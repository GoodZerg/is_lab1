package com.is.back.services;

import com.is.back.dto.AverageMetersAboveSeaLevelDTO;
import com.is.back.dto.CityDTO;
import com.is.back.dto.CoordinatesDTO;
import com.is.back.dto.HumanDTO;
import com.is.back.entity.City;
import com.is.back.exception.NotFoundException;
import com.is.back.repositories.CityRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialOperationsService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    CityRepository cityRepository;

    public AverageMetersAboveSeaLevelDTO calculateAverageMetersAboveSeaLevel() {
        /*StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("calculate_average_meters_above_sea_level");
        query.execute();
        double average = (double) query.getSingleResult();
        */
        Query query = entityManager.createNativeQuery("SELECT calculate_average_meters_above_sea_level()");
        Double average = (Double) query.getSingleResult();

        AverageMetersAboveSeaLevelDTO result = new AverageMetersAboveSeaLevelDTO();
        result.setAverage(average);
        return result;
    }

    public List<CityDTO> getCitiesByNameStartingWith(String prefix) {
       /* StoredProcedureQuery query = entityManager.createStoredProcedureQuery("find_cities_by_name_prefix");
        System.out.println(prefix);
        query.registerStoredProcedureParameter("prefix", String.class, ParameterMode.IN);
        query.setParameter("prefix", prefix.toCharArray());
        query.execute();*/

        //Query query = entityManager.createNativeQuery("SELECT * FROM find_cities_by_name_prefix( \'"+ prefix + "\')");

        List<City> cities = cityRepository.findByNameStartingWith(prefix);//query.getResultList();

        return cities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CityDTO> getCitiesByGovernorHeightLessThan(long height) {
        /*StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("find_cities_by_governor_height");
        query.setParameter("max_height", height);
        query.execute();
        */
        List<City> cities = cityRepository.findByGovernorLessThan(height);//query.getResultList();

        return cities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public String relocateAllPopulation(Long sourceCityId) {
        /*StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("relocate_all_population");
        query.setParameter("source_city_id", sourceCityId);
        query.execute();*/
        Query query = entityManager.createNativeQuery("SELECT relocate_all_population("+sourceCityId+")");
        return (String) query.getSingleResult();
    }

    public String relocateHalfOfCapitalPopulation() {
        /*StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("relocate_half_capital_population");
        query.execute();*/
        Query query = entityManager.createNativeQuery("SELECT relocate_half_capital_population()");
        return (String) query.getSingleResult();
    }

    private CityDTO convertToDTO(City city) {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(city.getId());
        cityDTO.setName(city.getName());
        cityDTO.setCoordinates(new CoordinatesDTO(city.getCoordinates().getId(), city.getCoordinates().getX(), city.getCoordinates().getY()));
        cityDTO.setCreationDate(city.getCreationDate());
        cityDTO.setArea(city.getArea());
        cityDTO.setPopulation(city.getPopulation());
        cityDTO.setEstablishmentDate(city.getEstablishmentDate());
        cityDTO.setCapital(city.isCapital());
        cityDTO.setMetersAboveSeaLevel(city.getMetersAboveSeaLevel());
        cityDTO.setClimate(city.getClimate());
        cityDTO.setGovernment(city.getGovernment());
        cityDTO.setStandardOfLiving(city.getStandardOfLiving());
        cityDTO.setGovernor(new HumanDTO(city.getGovernor().getId(), city.getGovernor().getHeight()));
        cityDTO.setUserId(city.getUser().getId());
        return cityDTO;
    }
}
