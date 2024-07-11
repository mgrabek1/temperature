package com.example.temperature.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVUtils {

    public static Map<Integer, Double> processCSV(String filePath, String city) throws IOException {
        Map<Integer, Double> temperatureSums = new HashMap<>();
        Map<Integer, Integer> temperatureCounts = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");

                if (fields[0].equalsIgnoreCase(city)) {
                    String[] dateParts = fields[1].split("-");
                    int year = Integer.parseInt(dateParts[0]);
                    double temperature = Double.parseDouble(fields[2]);

                    temperatureSums.merge(year, temperature, Double::sum);
                    temperatureCounts.merge(year, 1, Integer::sum);
                }
            }
        }

        Map<Integer, Double> yearlyAverageTemperatures = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : temperatureSums.entrySet()) {
            int year = entry.getKey();
            double averageTemperature = entry.getValue() / temperatureCounts.get(year);
            yearlyAverageTemperatures.put(year, averageTemperature);
        }

        return yearlyAverageTemperatures;
    }
}
