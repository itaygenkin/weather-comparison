package com.itay.weather.openweatherminer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.openweatherminer.dto.Location;
import com.itay.weather.openweatherminer.dto.WeatherSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class MinerService {

    private final RestTemplate restTemplate;
    @Value("${OPEN_WEATHER_API_KEY}")
    private final String apiKey;
    @Value("${OPEN_WEATHER_API_URL}")
    private final String apiUrl;

    @Autowired
    public MinerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.apiKey = System.getProperty("OPEN_WEATHER_API_KEY");
        this.apiUrl = System.getProperty("OPEN_WEATHER_API_URL");
    }

    public WeatherSample fetchAndSendData(Location location) {
        String json = fetchDataFromApi(location);
        return convertJsonToWeatherDataDto(json, location);
    }

    private String fetchDataFromApi(Location location) {
        String url = buildUrl(location, false);
        try {
            // TODO: process response
            ResponseEntity<String> response = restTemplate.getForEntity(
                    url, String.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    private String buildUrl(Location location, boolean locationByDegree){
//        if (locationByDegree)
//            return this.apiUrl + "?" + location.toStringByDegrees() + "&apikey=" + apiKey;
        // TODO: implement according to the open-weather format
        return this.apiUrl + "?apikey=" + apiKey;
    }

    private WeatherSample convertJsonToWeatherDataDto(String json, Location location) {
        // TODO: implement
        ObjectMapper objectMapper = new ObjectMapper();
        return null;
    }
}
