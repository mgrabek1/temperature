package com.example.temperature.mapper;

import com.example.temperature.dto.YearlyTemperatureDTO;
import com.example.temperature.model.YearlyTemperature;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface YearlyTemperatureMapper {

    YearlyTemperatureDTO yearlyTemperatureToYearlyTemperatureDTO(YearlyTemperature yearlyTemperature);

    List<YearlyTemperatureDTO> yearlyTemperaturesToYearlyTemperatureDTOs(List<YearlyTemperature> yearlyTemperatures);
}
