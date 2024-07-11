package com.example.temperature.controller;

import com.example.temperature.TemperatureApplication;
import com.example.temperature.dto.YearlyTemperatureDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TemperatureApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class TemperatureControllerIntegrationTest {
    @Value("${server.port}")
    private String port;

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void testGetYearlyAverageTemperatureForAnotherCity() {
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

    @Test
    public void testGetYearlyAverageTemperatureForCityNotFound() {
        String url = "http://localhost:" + port + "/yearly-average-temperature?city=UnknownCity";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body is null");
        assertEquals("City not found: UnknownCity", response.getBody());
    }

}
