package com.itay.weather.processor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.processor.dto.Location;
import com.itay.weather.processor.dto.WeatherList;
import com.itay.weather.processor.dto.WeatherPacket;
import com.itay.weather.processor.dto.WeatherSample;
import com.itay.weather.processor.model.WeatherSampleModel;
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
            WeatherSampleModel weatherData = convertWeatherSampleToWeathersampleModel(node);
            weatherRepository.save(weatherData);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private WeatherSampleModel convertWeatherSampleToWeathersampleModel(JsonNode node){
        // TODO: optimize so as to handle null values
        return WeatherSampleModel.builder()
                .source(node.get("source").asText())
//                .time(LocalDateTime.parse(node.get("time").asText()))
                .temperature(node.get("temperature").asDouble())
                .humidity(node.get("humidity").asDouble())
                .build();
    }

    public WeatherPacket getAllWeatherData(Location location){
        List<WeatherSampleModel> weatherSamples = weatherRepository.findAll();
        return buildWeatherPacketFromSamples(weatherSamples, location);
    }

    private WeatherPacket buildWeatherPacketFromSamples(List<WeatherSampleModel> weatherSamples, Location location){
        WeatherList accuList = new WeatherList("accu-weather", location);
        WeatherList openList = new WeatherList("open-weather", location);
        WeatherList tomorrowList = new WeatherList("tomorrow-weather", location);

        for (WeatherSampleModel weatherSample : weatherSamples){
            if (!weatherSample.getLocation().equals(location))
                continue;
            else if (weatherSample.getSource().equals("accu-weather"))
                accuList.addSample(convertWeatherSampleModelToWeatherSample(weatherSample));
            else if (weatherSample.getSource().equals("open-weather"))
                openList.addSample(convertWeatherSampleModelToWeatherSample(weatherSample));
            else if (weatherSample.getSource().equals("tomorrow-weather"))
                tomorrowList.addSample(convertWeatherSampleModelToWeatherSample(weatherSample));
        }

        return new WeatherPacket(accuList, openList, tomorrowList);
    }

    private WeatherSample convertWeatherSampleModelToWeatherSample(WeatherSampleModel weatherData){
        return WeatherSample.builder()
                .source(weatherData.getSource())
                .time(weatherData.getTime())
                .temperature(weatherData.getTemperature())
                .humidity(weatherData.getHumidity())
                .build();
    }

}
