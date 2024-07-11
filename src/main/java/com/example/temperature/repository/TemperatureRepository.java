package com.example.temperature.repository;

import com.example.temperature.exception.CSVProcessingException;
import com.example.temperature.exception.CityNotFoundException;
import com.example.temperature.model.YearlyTemperature;
import com.example.temperature.util.CSVUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TemperatureRepository {

    private final String filePath;

    public TemperatureRepository(@Value("${csv.file.path}") String filePath) {
        this.filePath = filePath;
    }

    public List<YearlyTemperature> findYearlyAverageTemperaturesByCity(String city) {
        try {
            Map<Integer, Double> yearlyTemperatures = CSVUtils.processCSV(filePath, city);

            if (yearlyTemperatures.isEmpty()) {
                throw new CityNotFoundException("City not found: " + city);
            }

            return yearlyTemperatures.entrySet().stream()
                    .map(entry -> new YearlyTemperature(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new CSVProcessingException("Error processing the CSV file", e);
        }
    }
}
