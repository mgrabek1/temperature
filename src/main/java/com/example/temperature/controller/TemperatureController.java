package com.example.temperature.controller;

import com.example.temperature.dto.YearlyTemperatureDTO;
import com.example.temperature.service.TemperatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TemperatureController {

    private final TemperatureService temperatureService;

    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @Operation(summary = "Get yearly average temperature for a city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved temperatures"),
            @ApiResponse(responseCode = "404", description = "City not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/yearly-average-temperature")
    public ResponseEntity<List<YearlyTemperatureDTO>> getYearlyAverageTemperature(@RequestParam String city) {
        List<YearlyTemperatureDTO> temperatures = temperatureService.getYearlyAverageTemperatures(city);
        if (temperatures.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(temperatures);
    }
}
