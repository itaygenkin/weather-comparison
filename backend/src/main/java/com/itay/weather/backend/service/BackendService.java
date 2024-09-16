package com.itay.weather.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.backend.dto.Location;
import com.itay.weather.backend.dto.MinerTriggerData;
import com.itay.weather.backend.dto.WeatherPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class BackendService {

    private final RestTemplate restTemplate;
    private final MinerTriggerData[] miners;

    @Autowired
    public BackendService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        miners = new MinerTriggerData[]{
                new MinerTriggerData("localhost:8091/", "accu-weather"),
                new MinerTriggerData("localhost:8092/", "tomorrow"),
                new MinerTriggerData("localhost:8093/", "open-weather")};
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
            return response.getBody();
        }
        catch (RestClientException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean trigger(Location location) {
        try {
            for (MinerTriggerData miner : miners) {
                // creating url
                String url = "http://" + miner.getUrl() + "/miner/" + miner.getMinerName();

                // creating headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // creating body to the post request
                Map<String, String> bodyParams = new HashMap<>();
                bodyParams.put("city", location.getCity());
                bodyParams.put("country", location.getCountry());

                // creating request entity
                String reqBodyData = new ObjectMapper().writeValueAsString(bodyParams);
                HttpEntity<String> requestEntity = new HttpEntity<>(reqBodyData, headers);

                ResponseEntity<Void> response = restTemplate.postForEntity(
                        url,
                        requestEntity,
                        Void.class
                );
                System.out.println("response: " + response);
                return response.getStatusCode() == HttpStatus.OK;
            }
        }
        catch (RestClientException | JsonProcessingException e){
            System.out.println("got an error:   " + e.getMessage());

            return false;
        }
        return true;
    }
}
