package com.itay.weather.openweatherminer.producer;

import com.itay.weather.openweatherminer.dto.WeatherDataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MinerProducer {

    private final KafkaTemplate<String, WeatherDataDto> kafkaTemplate;
    @Value("${spring.kafka.producer.topic}")
    private String topic;

    public MinerProducer(final KafkaTemplate<String, WeatherDataDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDataToKafka(WeatherDataDto weatherData) {
        kafkaTemplate.send(topic, weatherData);
    }

}
