package com.itay.weather.tomorrowminer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.tomorrowminer.dto.ApiResponse;
import com.itay.weather.tomorrowminer.dto.ApiResponseData;
import com.itay.weather.tomorrowminer.dto.Location;
import com.itay.weather.tomorrowminer.dto.WeatherSample;
import com.itay.weather.tomorrowminer.producer.MinerProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MinerService {

    private final RestTemplate restTemplate;
    private final MinerProducer minerProducer;
    @Value("${TOMORROW_WEATHER_API_KEY}")
    private final String apiKey;
    @Value("${TOMORROW_WEATHER_API_URL}")
    private final String apiUrl;

    @Autowired
    public MinerService(RestTemplate restTemplate, MinerProducer minerProducer) {
        this.restTemplate = restTemplate;
        this.minerProducer = minerProducer;
        this.apiKey = System.getProperty("TOMORROW_WEATHER_API_KEY");
        this.apiUrl = System.getProperty("TOMORROW_WEATHER_API_URL");
    }

    public boolean fetchAndSendData(Location location) {
        String json = fetchDataFromApi(location);
        if (json == null)
            return false;
        return minerProducer.sendDataToKafka(convertJsonToWeatherSample(json, location));
    }

    private String fetchDataFromApi(Location location) {
        boolean b = Math.random() < 0.5;  // temporary for development
        String tomorrowApi = buildUrl(location, b);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(tomorrowApi, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.info("failed to request weather data from tomorrow api");
                return null;
            }
            log.info("response status: {}", response.getStatusCode());
            return response.getBody();
        } catch (RestClientException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private String buildUrl(Location location, boolean locationByDegree){
        if (locationByDegree)
            return this.apiUrl + "?" + location.toStringByDegrees() + "&apikey=" + apiKey;
        return this.apiUrl + "?" + location.toString() + "&apikey=" + apiKey;
    }

    private WeatherSample convertJsonToWeatherSample(String json, Location location) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ApiResponseData responseData = objectMapper.readValue(json, ApiResponse.class).getData();
            return WeatherSample.builder()
                    .source("tomorrow-weather")
                    .time(responseData.getTime())
                    .location(location)
                    .temperature(responseData.getValues().getTemperature())
                    .humidity(responseData.getValues().getHumidity())
                    .build();
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
