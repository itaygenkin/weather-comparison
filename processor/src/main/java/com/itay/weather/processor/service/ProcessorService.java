package com.itay.weather.processor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherPacket;
import com.itay.weather.dto.WeatherSample;
import com.itay.weather.processor.model.LocationModel;
import com.itay.weather.processor.model.WeatherSampleModel;
import com.itay.weather.processor.repository.LocationRepository;
import com.itay.weather.processor.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProcessorService {

    private final WeatherRepository weatherRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public ProcessorService(WeatherRepository weatherRepository, LocationRepository locationRepository) {
        this.weatherRepository = weatherRepository;
        this.locationRepository = locationRepository;
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

    public List<WeatherSample> getWeatherData() {
        return weatherRepository.findAll().stream().map(WeatherSampleModel::toWeatherSample).toList();
    }

    public WeatherPacket getWeatherDataByLocation(Location location) {
        List<WeatherSample> weatherSamples = weatherRepository.findAll()
                .stream()
                .map(WeatherSampleModel::toWeatherSample)
                .toList();
        return new WeatherPacket(weatherSamples, location);  // WeatherPacket constructor filters the samples by location
    }

    public WeatherPacket getWeatherDataByLocationAndTime(Location location, LocalDateTime start, LocalDateTime end) {
        List<WeatherSample> weatherSamples = weatherRepository.findAllByTimeBetween(start, end)
                .stream()
                .map(WeatherSampleModel::toWeatherSample)
                .toList();
        return new WeatherPacket(weatherSamples, location, start, end);
    }

    public void deleteWeatherDataByLocation(Location location) {
        weatherRepository.deleteAll(weatherRepository.getAllByLocation(location));
    }

    public boolean addLocation(Location location) {
        // check if city is null or empty
        if (location.getCity() == null || location.getCity().isEmpty())
            return false;
        // check if country is null or empty
        if (location.getCountry() == null || location.getCountry().isEmpty())
            return false;
        // check if latitude or longitude didn't assigned
        if (location.getLatitude() == 0.0 || location.getLongitude() == 0.0)
            return false;

        List<LocationModel> locationModelList = locationRepository.findAll();
        for (LocationModel locationModel : locationModelList) {
            if (locationModel.getCity().equals(location.getCity()) && locationModel.getCountry().equals(location.getCountry())) {
                return false;
            }
        }

        LocationModel locationModel = LocationModel.builder()
                .city(location.getCity())
                .country(location.getCountry())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build();
        locationRepository.save(locationModel);
        log.info("new location has been saved: {}", location);
        return true;
    }

    public List<LocationModel> getLocations() {
        return locationRepository.findAll();
    }

    private static Location toLocation(LocationModel locationModel) {
        return Location.builder()
                .city(locationModel.getCity())
                .country(locationModel.getCountry())
                .longitude(locationModel.getLongitude())
                .latitude(locationModel.getLatitude())
                .build();
    }

    public void deleteCities() {
        locationRepository.deleteAll();
    }

    public void deleteCity(long id) {
        Optional<LocationModel> locationToDelete = locationRepository.findById(id);
        String toLog = locationToDelete.map(locationModel -> "deleting location model: " + locationModel)
                .orElseGet(() -> String.format("tried to delete location with id(%d) but not found", id));

        locationRepository.deleteById(id);
        log.info(toLog);
    }
}
