package com.itay.weather.tomorrowminer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.tomorrowminer.dto.WeatherDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class MinerService {

    private final RestTemplate restTemplate;
    private final String apiKey = "";
    private final String apiUrl = "";

    @Autowired
    public MinerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
//        this.apiKey = System.getProperty("ACCU_WEATHER_API_KEY");
//        this.apiUrl = System.getProperty("ACCU_WEATHER_API_URL");
    }

    public WeatherDataDto fetchAndSendData() {
        String json = fetchDataFromApi();
        return convertJsonToWeatherDataDto(json);
    }

    private String fetchDataFromApi() {
        String tomorrowApi = apiUrl + apiKey;
        try {
            // TODO: process response
            ResponseEntity<String> response = restTemplate.getForEntity(
                    tomorrowApi, String.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    private WeatherDataDto convertJsonToWeatherDataDto(String json) {
        // TODO: implement
        ObjectMapper objectMapper = new ObjectMapper();
        return null;
    }
}
