package com.itay.weather.backend.service;

import com.itay.weather.backend.dto.Location;
import com.itay.weather.backend.dto.MinerTriggerData;
import com.itay.weather.backend.dto.WeatherPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

    public boolean trigger() {
        try {
            for (MinerTriggerData miner : miners) {
                // @TODO: fix post request
                restTemplate.exchange(miner.getUrl(), HttpMethod.POST, null,
                        new ParameterizedTypeReference<Void>() {});
            }
        }
        catch (RestClientException e){
            return false;
        }
        return true;
    }
}
