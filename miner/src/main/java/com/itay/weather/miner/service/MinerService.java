package com.itay.weather.miner.service;

import com.itay.weather.miner.producer.MinerProducer;
import dto.AbstractMiner;
import dto.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class MinerService {

    private final RestTemplate restTemplate;
    private final MinerProducer minerProducer;
    private final List<AbstractMiner> miners = new ArrayList<>();

    @Autowired
    public MinerService(RestTemplate restTemplate, MinerProducer minerProducer) {
        this.restTemplate = restTemplate;
        this.minerProducer = minerProducer;
        // TODO: initialize miners
    }


    // TODO: throw exception when needed
    // TODO: use threads for concurrency
    public void fetchAndSendData(Location location) {
        for (AbstractMiner miner : miners) {
            String json = fetchDataFromApi(miner, location);
            if (json != null)
                continue;
            minerProducer.sendDataToKafka(miner.processResponse(json));
        }
    }

    private String fetchDataFromApi(AbstractMiner miner, Location location) {
        HashMap<String, String> params = new HashMap<>();
        params.put("city", location.getCity());
        params.put("country", location.getCountry());
        String url = miner.buildUrl(params);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (!response.getStatusCode().is2xxSuccessful()){
                log.info("failed to request weather data from tomorrow api");
                return null;
            }
            log.info("repsonse status: {}", response.getStatusCode());
            return response.getBody();
        } catch (RestClientException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
