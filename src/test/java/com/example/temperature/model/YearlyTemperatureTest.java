package com.example.temperature.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class YearlyTemperatureTest {

    @Test
    public void testYearlyTemperatureConstructorAndGetters() {
        YearlyTemperature yearlyTemperature = new YearlyTemperature(2021, 12.1);

        assertEquals(2021, yearlyTemperature.year());
        assertEquals(12.1, yearlyTemperature.averageTemperature());
    }

}
