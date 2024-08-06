package com.itay.weather.accuweatherminer.producer;

import com.itay.weather.accuweatherminer.dto.WeatherSample;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MinerProducer {

    private final KafkaTemplate<String, WeatherSample> kafkaTemplate;
    @Value("${spring.kafka.producer.topic}")
    private String topic;

    public MinerProducer(final KafkaTemplate<String, WeatherSample> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDataToKafka(WeatherSample weatherData) {
        kafkaTemplate.send(topic, weatherData);
    }

}
