package com.itay.weather.accuweatherminer.service;

import com.itay.weather.accuweatherminer.dto.WeatherDataDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MinerService {

    private final KafkaTemplate<String, WeatherDataDto> kafkaTemplate;

    public MinerService(KafkaTemplate<String, WeatherDataDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void fetchAndSendData() {
        WeatherDataDto data = fetchDataFromApi();
        sendDataToKafka(data);
//        kafkaTemplate.send("weatherData", "testing");
    }

    private WeatherDataDto fetchDataFromApi() {
        String accuApi = "";
        // TODO: implement
        return WeatherDataDto
                .builder()
                .source("accu-weather")
                .humidity(70.0F)
                .temperature(30.0F)
                .build();
    }

    private void sendDataToKafka(WeatherDataDto weatherData) {
        kafkaTemplate.send("weatherData", weatherData);
    }

}
