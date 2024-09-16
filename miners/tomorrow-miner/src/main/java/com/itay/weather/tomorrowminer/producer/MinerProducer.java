package com.itay.weather.tomorrowminer.producer;

import com.itay.weather.tomorrowminer.dto.WeatherSample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MinerProducer {

    private final KafkaTemplate<String, WeatherSample> kafkaTemplate;
    @Value("${spring.kafka.producer.topic}")
    private String topic;

    public MinerProducer(KafkaTemplate<String, WeatherSample> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean sendDataToKafka(WeatherSample weatherSample) {
        log.info("Sending data to topic {};", topic);
        System.out.println(weatherSample);
        try {
            kafkaTemplate.send(topic, weatherSample);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
        return true;
    }

}
