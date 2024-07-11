package com.example.temperature.service;

import com.example.temperature.dto.YearlyTemperatureDTO;
import com.example.temperature.exception.CityNotFoundException;
import com.example.temperature.mapper.YearlyTemperatureMapper;
import com.example.temperature.model.YearlyTemperature;
import com.example.temperature.repository.TemperatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemperatureService {

    private final TemperatureRepository temperatureRepository;
    private final YearlyTemperatureMapper yearlyTemperatureMapper;

    public TemperatureService(TemperatureRepository temperatureRepository, YearlyTemperatureMapper yearlyTemperatureMapper) {
        this.temperatureRepository = temperatureRepository;
        this.yearlyTemperatureMapper = yearlyTemperatureMapper;
    }

    public List<YearlyTemperatureDTO> getYearlyAverageTemperatures(String city) {
        List<YearlyTemperature> yearlyTemperatures = temperatureRepository.findYearlyAverageTemperaturesByCity(city);
        if (yearlyTemperatures.isEmpty()) {
            throw new CityNotFoundException("City not found: " + city);
        }
        return yearlyTemperatureMapper.yearlyTemperaturesToYearlyTemperatureDTOs(yearlyTemperatures);
    }
}
