package com.itay.weather.processor.service;

import com.itay.weather.processor.configuration.BackendProperties;
import com.itay.weather.processor.dto.WeatherDataDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProcessorService {

    private final RestTemplate restTemplate;
    private final BackendProperties backendProperties;

    @Autowired
    public ProcessorService(RestTemplate restTemplate, BackendProperties backendProperties){
        this.restTemplate = restTemplate;
        this.backendProperties = backendProperties;
    }

    @KafkaListener(topics = "weather-data")
    public void process(ConsumerRecord<String, WeatherDataDto> record){
        System.out.println(backendProperties.getBaseUrl());
        restTemplate.postForObject("http://localhost:8080/api/save-data", record.value(), WeatherDataDto.class);
    }

}