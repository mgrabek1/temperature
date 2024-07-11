package com.example.temperature.controller;

import com.example.temperature.TemperatureApplication;
import com.example.temperature.dto.YearlyTemperatureDTO;
import com.example.temperature.exception.CityNotFoundException;
import com.example.temperature.service.TemperatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TemperatureApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TemperatureControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private TemperatureService temperatureService;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        List<YearlyTemperatureDTO> mockResponse = Arrays.asList(
                new YearlyTemperatureDTO(2021, 12.0),
                new YearlyTemperatureDTO(2022, 15.0)
        );
        when(temperatureService.getYearlyAverageTemperatures(anyString())).thenReturn(mockResponse);
    }

    @Test
    public void testGetYearlyAverageTemperature() {
        String url = "http://localhost:" + port + "/yearly-average-temperature?city=TestCity";
        ResponseEntity<YearlyTemperatureDTO[]> response = restTemplate.getForEntity(url, YearlyTemperatureDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body is null");

        YearlyTemperatureDTO[] temperatures = response.getBody();
        assertNotNull(temperatures, "Temperatures array is null");
        assertEquals(2, temperatures.length);

        assertEquals(2021, temperatures[0].year());
        assertEquals(12.0, temperatures[0].averageTemperature(), 0.0001);
        assertEquals(2022, temperatures[1].year());
        assertEquals(15.0, temperatures[1].averageTemperature(), 0.0001);
    }

    @Test
    public void testGetYearlyAverageTemperatureForCityNotFound() {
        when(temperatureService.getYearlyAverageTemperatures(anyString())).thenThrow(new CityNotFoundException("City not found: TestCity"));

        String url = "http://localhost:" + port + "/yearly-average-temperature?city=TestCity";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("City not found: TestCity", response.getBody());
    }

    @Test
    public void testGetYearlyAverageTemperatureForAnotherCity() {
        List<YearlyTemperatureDTO> mockResponse = Arrays.asList(
                new YearlyTemperatureDTO(2021, 20.0)
        );
        when(temperatureService.getYearlyAverageTemperatures("AnotherCity")).thenReturn(mockResponse);

        String url = "http://localhost:" + port + "/yearly-average-temperature?city=AnotherCity";
        ResponseEntity<YearlyTemperatureDTO[]> response = restTemplate.getForEntity(url, YearlyTemperatureDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body is null");

        YearlyTemperatureDTO[] temperatures = response.getBody();
        assertNotNull(temperatures, "Temperatures array is null");
        assertEquals(1, temperatures.length);

        assertEquals(2021, temperatures[0].year());
        assertEquals(20.0, temperatures[0].averageTemperature(), 0.0001);
    }
}
