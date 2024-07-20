package com.itay.weather.backend.service;

import com.itay.weather.backend.dto.WeatherDataDto;
import com.itay.weather.backend.model.WeatherData;
import com.itay.weather.backend.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BackendService {

    private final WeatherRepository weatherRepository;

    @Autowired
    public BackendService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public void saveData(List<WeatherDataDto> weatherDataList){
        // TODO: implement
        weatherDataList.stream().map(this::convertWeatherDataDtoToWeatherData).forEach(weatherRepository::save);
    }

    private WeatherData convertWeatherDataDtoToWeatherData(WeatherDataDto weatherDataDto){
        return WeatherData.builder()
                .source(weatherDataDto.getSource())
                .time(weatherDataDto.getTime())
                .temperature(weatherDataDto.getTemperature())
                .humidity(weatherDataDto.getHumidity())
                .build();
    }

    public List<WeatherDataDto> getWeatherData() {
        // TODO: implement
        return new ArrayList<>();
    }
}
