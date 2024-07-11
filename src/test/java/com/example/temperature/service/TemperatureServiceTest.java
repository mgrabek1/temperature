package com.example.temperature.service;

import com.example.temperature.dto.YearlyTemperatureDTO;
import com.example.temperature.exception.CityNotFoundException;
import com.example.temperature.model.YearlyTemperature;
import com.example.temperature.repository.TemperatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TemperatureServiceTest {

    @MockBean
    private TemperatureRepository temperatureRepository;

    @Autowired
    private TemperatureService temperatureService;

    @BeforeEach
    public void setUp() {
        List<YearlyTemperature> mockData = Arrays.asList(
                new YearlyTemperature(2021, 12.0),
                new YearlyTemperature(2022, 15.0)
        );
        when(temperatureRepository.findYearlyAverageTemperaturesByCity(anyString())).thenReturn(mockData);
    }

    @Test
    public void testGetYearlyAverageTemperatures() {
        List<YearlyTemperatureDTO> result = temperatureService.getYearlyAverageTemperatures("TestCity");

        assertEquals(2, result.size());
        assertEquals(2021, result.get(0).year());
        assertEquals(12.0, result.get(0).averageTemperature(), 0.0001);
        assertEquals(2022, result.get(1).year());
        assertEquals(15.0, result.get(1).averageTemperature(), 0.0001);
    }

    @Test
    public void testGetYearlyAverageTemperaturesForCityNotFound() {
        when(temperatureRepository.findYearlyAverageTemperaturesByCity(anyString())).thenReturn(Arrays.asList());

        CityNotFoundException exception = assertThrows(CityNotFoundException.class, () -> {
            temperatureService.getYearlyAverageTemperatures("UnknownCity");
        });

        assertEquals("City not found: UnknownCity", exception.getMessage());
    }

    @Test
    public void testGetYearlyAverageTemperaturesForAnotherCity() {
        List<YearlyTemperature> mockData = Arrays.asList(
                new YearlyTemperature(2021, 20.0)
        );
        when(temperatureRepository.findYearlyAverageTemperaturesByCity("AnotherCity")).thenReturn(mockData);

        List<YearlyTemperatureDTO> result = temperatureService.getYearlyAverageTemperatures("AnotherCity");

        assertEquals(1, result.size());
        assertEquals(2021, result.get(0).year());
        assertEquals(20.0, result.get(0).averageTemperature(), 0.0001);
    }
}
