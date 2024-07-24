package com.itay.weather.processor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.processor.dto.WeatherDataDto;
import com.itay.weather.processor.model.WeatherData;
import com.itay.weather.processor.repository.WeatherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessorService {

    private final WeatherRepository weatherRepository;

    @Autowired
    public ProcessorService(WeatherRepository weatherRepository){
        this.weatherRepository = weatherRepository;
    }

    @KafkaListener(topics = "weather-data")
    public void handleWeatherData(@Payload String data /*WeatherDataDto data*/){
        System.out.println("processing...");
        System.out.println(data);
        // process data if needed and then:
        try {
            JsonNode node = new ObjectMapper().readTree(data);
            WeatherData weatherData = convertWeatherDataDtoToWeatherData(node );
            weatherRepository.save(weatherData);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private WeatherData convertWeatherDataDtoToWeatherData(JsonNode node){
        // TODO: optimize so as to handle null values
        return WeatherData.builder()
                .source(node.get("source").asText())
//                .time(LocalDateTime.parse(node.get("time").asText()))
                .temperature(node.get("temperature").asDouble())
                .humidity(node.get("humidity").asDouble())
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