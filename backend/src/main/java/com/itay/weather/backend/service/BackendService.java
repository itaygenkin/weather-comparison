package com.itay.weather.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BackendService {

    private final RestTemplate restTemplate;

    @Autowired
    public BackendService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherPacket getWeatherData(Location location) {
        String url = "http://localhost:8081/api/weather?city={city}&country={country}";
        try {
            ResponseEntity<WeatherPacket> response = restTemplate.getForEntity(
                    url,
                    WeatherPacket.class,
                    location.getCity(),
                    location.getCountry()
            );
            log.info("response status: {}", response.getStatusCode());
            return response.getBody();
        }
        catch (RestClientException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean trigger(Location location) {
        String url = "http://localhost:8082/miner/fetch";
        try {
            // creating url
            HttpEntity<String> requestEntity = createHttpEntity(location);
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    url,
                    requestEntity,
                    Void.class
            );
            log.info("response status: {}", response.getStatusCode());
            return response.getStatusCode() == HttpStatus.OK;
        }
        catch (RestClientException | JsonProcessingException e){
            log.error(e.getMessage());
            return false;
        }
    }

    private static HttpEntity<String> createHttpEntity(Location location) throws JsonProcessingException {
        // creating headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // creating body to the post request
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("city", location.getCity());
        bodyParams.put("country", location.getCountry());

        // creating request entity
        String reqBodyData = new ObjectMapper().writeValueAsString(bodyParams);
        return new HttpEntity<>(reqBodyData, headers);
    }
}
