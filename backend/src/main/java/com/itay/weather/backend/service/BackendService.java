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

    public WeatherPacket getWeatherData(Location location, String start, String end) {
        // TODO: use UriComponentsBuilder
        String url = processorProperties.getBaseUrl() +
                "weather?city={city}&country={country}&start={start}&end={end}";
        try {
            ResponseEntity<WeatherPacket> response = restTemplate.getForEntity(
                    url,
                    WeatherPacket.class,
                    location.getCity(),
                    location.getCountry(),
                    start,
                    end
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
        String url = minerProperties.getBaseUrl();
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
