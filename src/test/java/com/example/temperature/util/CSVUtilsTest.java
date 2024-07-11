package com.example.temperature.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CSVUtilsTest {

    @Test
    public void testProcessCSV() throws IOException {
        String content = "city;yyyy-mm-dd HH:mm:ss.SSS;temp\n" +
                "TestCity;2021-01-01 00:00:00.000;10.0\n" +
                "TestCity;2021-02-01 00:00:00.000;14.0\n" +
                "TestCity;2022-01-01 00:00:00.000;15.0\n";

        Path tempFile = Files.createTempFile("test", ".csv");
        Files.write(tempFile, content.getBytes());

        Map<Integer, Double> result = CSVUtils.processCSV(tempFile.toString(), "TestCity");

        assertEquals(2, result.size());
        assertEquals(12.0, result.get(2021));
        assertEquals(15.0, result.get(2022));

        Files.delete(tempFile);
    }

    @Test
    public void testProcessCSVNoDataForCity() throws IOException {
        String content = "city;yyyy-mm-dd HH:mm:ss.SSS;temp\n" +
                "AnotherCity;2021-01-01 00:00:00.000;10.0\n";

        Path tempFile = Files.createTempFile("test", ".csv");
        Files.write(tempFile, content.getBytes());

        Map<Integer, Double> result = CSVUtils.processCSV(tempFile.toString(), "TestCity");

        assertTrue(result.isEmpty());

        Files.delete(tempFile);
    }
}
