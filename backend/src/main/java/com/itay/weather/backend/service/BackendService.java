package com.itay.weather.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.backend.configuration.MinerProperties;
import com.itay.weather.backend.configuration.ProcessorProperties;
import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BackendService {

    private final RestTemplate restTemplate;
    private final ProcessorProperties processorProperties;
    private final MinerProperties minerProperties;

    @Autowired
    public BackendService(RestTemplate restTemplate, ProcessorProperties processorProperties, MinerProperties minerProperties) {
        this.restTemplate = restTemplate;
        this.processorProperties = processorProperties;
        this.minerProperties = minerProperties;
    }

    public WeatherPacket getWeatherData(Location location, String from, String to) {
        String uri = UriComponentsBuilder.fromHttpUrl(processorProperties.getBaseUrl())
                .path("weather")
                .queryParam("city", location.getCity())
                .queryParam("country", location.getCountry())
                .queryParam("from", from)
                .queryParam("to", to)
                .toUriString();

        try {
            ResponseEntity<WeatherPacket> response = restTemplate.getForEntity(uri, WeatherPacket.class);
            log.info("response status: {}", response.getStatusCode());
            return response.getBody();
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean trigger(Location location) {
        // TODO: use UriComponentBuilder
        String uri = UriComponentsBuilder
                .fromHttpUrl(minerProperties.getBaseUrl())
                .path("fetch")
                .toUriString();
        try {
            // create http entity
            HttpEntity<String> httpEntity = createHttpEntity(location);
            ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, Void.class);

            log.info("response status: {}", response.getStatusCode());

            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException | JsonProcessingException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private static HttpEntity<String> createHttpEntity(Location location) throws JsonProcessingException {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // create body to the post request
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("city", location.getCity());
        bodyParams.put("country", location.getCountry());

        // create request entity
        String reqBodyData = new ObjectMapper().writeValueAsString(bodyParams);
        return new HttpEntity<>(reqBodyData, headers);
    }
}
