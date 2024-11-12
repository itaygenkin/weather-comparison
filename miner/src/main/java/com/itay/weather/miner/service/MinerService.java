package com.itay.weather.miner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itay.weather.miner.component.MinerList;
import com.itay.weather.miner.producer.MinerProducer;
import com.itay.weather.dto.AbstractMiner;
import com.itay.weather.dto.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MinerService {

    private final RestTemplate restTemplate;
    private final MinerProducer minerProducer;
    private final MinerList miners;

    @Autowired
    public MinerService(RestTemplate restTemplate, MinerProducer minerProducer, MinerList miners) {
        this.restTemplate = restTemplate;
        this.minerProducer = minerProducer;
        this.miners = miners;
    }

    // TODO: use threads for concurrency or timeout
    public void fetchAndSendData(Location location) {
        for (AbstractMiner miner : miners.getMiners()) {
            String json = fetchDataFromApi(miner, location);
            if (json == null)
                continue;
            try {
                minerProducer.sendDataToKafka(miner.processResponse(json, location));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private String fetchDataFromApi(AbstractMiner miner, Location location) {
        String url = miner.buildUrl(location.toHashMap());
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (!response.getStatusCode().is2xxSuccessful()){
                log.info("failed to request weather data from tomorrow api");
                return null;
            }
            log.info("response status: {}", response.getStatusCode());
            return response.getBody();
        } catch (RestClientException | IllegalArgumentException e ) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
