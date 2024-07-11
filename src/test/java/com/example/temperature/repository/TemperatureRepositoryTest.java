package com.example.temperature.repository;

import com.example.temperature.exception.CSVProcessingException;
import com.example.temperature.exception.CityNotFoundException;
import com.example.temperature.model.YearlyTemperature;
import com.example.temperature.util.CSVUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class TemperatureRepositoryTest {

    private TemperatureRepository temperatureRepository;

    @Value("${csv.file.path}")
    private String filePath;

    @BeforeEach
    public void setUp() {
        temperatureRepository = new TemperatureRepository(filePath);
    }

    @Test
    public void testFindYearlyAverageTemperaturesByCity() {
        Map<Integer, Double> mockData = new HashMap<>();
        mockData.put(2021, 12.0);
        mockData.put(2022, 15.0);

        try (MockedStatic<CSVUtils> mockedCSVUtils = Mockito.mockStatic(CSVUtils.class)) {
            mockedCSVUtils.when(() -> CSVUtils.processCSV(anyString(), eq("TestCity"))).thenReturn(mockData);

            List<YearlyTemperature> result = temperatureRepository.findYearlyAverageTemperaturesByCity("TestCity");

            assertEquals(2, result.size());
            assertEquals(12.0, result.get(0).averageTemperature(), 0.0001);
            assertEquals(15.0, result.get(1).averageTemperature(), 0.0001);
        }
    }

    @Test
    public void testFindYearlyAverageTemperaturesByCity_CityNotFound() {
        Map<Integer, Double> mockData = new HashMap<>();

        try (MockedStatic<CSVUtils> mockedCSVUtils = Mockito.mockStatic(CSVUtils.class)) {
            mockedCSVUtils.when(() -> CSVUtils.processCSV(anyString(), eq("UnknownCity"))).thenReturn(mockData);

            CityNotFoundException exception = assertThrows(CityNotFoundException.class, () -> temperatureRepository.findYearlyAverageTemperaturesByCity("UnknownCity"));

            assertEquals("City not found: UnknownCity", exception.getMessage());
        }
    }

    @Test
    public void testFindYearlyAverageTemperaturesByCity_CSVProcessingException() {
        try (MockedStatic<CSVUtils> mockedCSVUtils = Mockito.mockStatic(CSVUtils.class)) {
            mockedCSVUtils.when(() -> CSVUtils.processCSV(anyString(), eq("TestCity"))).thenThrow(new IOException("IO error"));

            CSVProcessingException exception = assertThrows(CSVProcessingException.class, () -> temperatureRepository.findYearlyAverageTemperaturesByCity("TestCity"));

            assertEquals("Error processing the CSV file", exception.getMessage());
        }
    }
}
