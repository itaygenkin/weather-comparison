package com.itay.weather.miner.producer;

import dto.WeatherSample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MinerProducer {

    private final KafkaTemplate<String, WeatherSample> kafkaTemplate;
    @Value("weather-data")
    private String topic;

    public MinerProducer(KafkaTemplate<String, WeatherSample> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDataToKafka(WeatherSample weatherSample) {
        log.info("Sending data to kafka topic: {};", topic);
        try {
            kafkaTemplate.send(topic, weatherSample);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
