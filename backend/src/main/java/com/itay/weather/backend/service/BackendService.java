package com.itay.weather.backend.service;

import com.itay.weather.backend.dto.WeatherDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BackendService {

    private final RestTemplate restTemplate;
    private final String[] minerNames = {"accu-weather"/*, "tomorrow", "open-weather"*/};

    @Autowired
    public BackendService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<WeatherDataDto> getWeatherData() {
        String url = "http://localhost:8081/api/weather";
        try {
            ResponseEntity<List<WeatherDataDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<WeatherDataDto>>() {}
            );
            return response.getBody();
        }
        catch (RestClientException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean trigger() {
        String baseUrl = "http://localhost:8091/";
        try {
            for (String minerName : minerNames) {
                restTemplate.exchange(baseUrl + minerName, HttpMethod.POST, null,
                        new ParameterizedTypeReference<Void>() {});
            }
        }
        catch (RestClientException e){
            return false;
        }
        return true;
    }
}