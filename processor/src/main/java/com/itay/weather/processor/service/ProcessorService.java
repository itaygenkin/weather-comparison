package com.itay.weather.processor.service;

import com.itay.weather.processor.dto.WeatherDataDto;
import com.itay.weather.processor.model.WeatherData;
import com.itay.weather.processor.repository.WeatherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessorService {

    private final WeatherRepository weatherRepository;

    @Autowired
    public ProcessorService(WeatherRepository weatherRepository){
        this.weatherRepository = weatherRepository;
    }

    @KafkaListener(topics = "weatherData")
    public void handleWeatherData(List<WeatherDataDto> dataList){
        System.out.println("processing...");
        System.out.println(dataList);
        // process data if needed and then:
        List<WeatherData> processedDataList = dataList.stream().map(this::convertWeatherDataDtoToWeatherData).toList();
        weatherRepository.saveAll(processedDataList);
    }

    private WeatherData convertWeatherDataDtoToWeatherData(WeatherDataDto weatherData){
        return WeatherData.builder()
                .source(weatherData.getSource())
                .time(weatherData.getTime())
                .temperature(weatherData.getTemperature())
                .humidity(weatherData.getHumidity())
                .build();
    }

    public List<WeatherDataDto> getAllWeatherData(){
        return weatherRepository.findAll().stream().map(this::convertWeatherDataToWeatherDataDto).toList();
    }

    private WeatherDataDto convertWeatherDataToWeatherDataDto(WeatherData weatherData){
        return WeatherDataDto.builder()
                .source(weatherData.getSource())
                .time(weatherData.getTime())
                .temperature(weatherData.getTemperature())
                .humidity(weatherData.getHumidity())
                .build();
    }

}