package com.itay.weather.processor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherPacket;
import com.itay.weather.dto.WeatherSample;
import com.itay.weather.processor.model.WeatherSampleModel;
import com.itay.weather.processor.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ProcessorService {

    private final WeatherRepository weatherRepository;

    @Autowired
    public ProcessorService(WeatherRepository weatherRepository){
        this.weatherRepository = weatherRepository;
    }

    @KafkaListener(topics = "weather-data", groupId = "miner-group")
    public void consume(@Payload String data){
        log.info("received weather data: {}", data);
        // process data if needed and then:
        try {
            JsonNode node = new ObjectMapper().readTree(data);
            WeatherSampleModel weatherData = buildWeathersampleModel(node);
            weatherRepository.save(weatherData);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }

    private WeatherSampleModel buildWeathersampleModel(JsonNode node) throws NullPointerException {
        // TODO: optimize to handle null values
        Location location = Location.builder()
                .city(node.get("location").get("city").asText())
                .country(node.get("location").get("country").asText())
                .latitude(node.get("location").get("latitude").asDouble())
                .longitude(node.get("location").get("longitude").asDouble())
                .build();

        LocalDateTime time = toLocalDateTime(node.get("time").toString());

        return WeatherSampleModel.builder()
                .source(node.get("source").asText())
                .time(time)
                .location(location)
                .temperature(node.get("temperature").asDouble())
                .humidity(node.get("humidity").asInt())
                .build();
    }

    private static LocalDateTime toLocalDateTime(String time) throws NullPointerException, IllegalArgumentException {
        if (time == null || time.isEmpty())
            throw new NullPointerException();

        String[] timeValues = time.substring(1, time.length() - 1).split(",");
        List<Integer> intTimeValues = Arrays.stream(timeValues).map(Integer::valueOf).toList();

        if (timeValues.length != 5)
            throw new IllegalArgumentException("cannot parse time string <" + time + ">");

        return LocalDateTime.of(
                intTimeValues.get(0),
                intTimeValues.get(1),
                intTimeValues.get(2),
                intTimeValues.get(3),
                intTimeValues.get(4)
        );
    }

    public WeatherPacket getWeatherData(Location location){
        List<WeatherSample> weatherSamples = weatherRepository.findAll()
                .stream()
                .map(WeatherSampleModel::toWeatherSample)
                .toList();
        return new WeatherPacket(weatherSamples, location);
    }

    public WeatherPacket getWeatherDataByLocation(Location location){
        List<WeatherSample> weatherSamples = weatherRepository.findAllByLocation(location)
                .stream()
                .map(WeatherSampleModel::toWeatherSample)
                .toList();
        return new WeatherPacket(weatherSamples, location);
    }

    public WeatherPacket getWeatherDataByLocationAndTime(Location location, Timestamp start, Timestamp end){
        List<WeatherSample> weatherSamples = weatherRepository.findAllByLocationAndTimeBetween(location, start, end)
                .stream()
                .map(WeatherSampleModel::toWeatherSample)
                .toList();
        return new WeatherPacket(weatherSamples, location);
    }

    public void deleteAll(){
        weatherRepository.deleteAll();
    }
}
