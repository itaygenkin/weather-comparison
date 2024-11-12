package com.itay.weather.dto;

import com.itay.weather.api_objects.ApiResponse;
import com.itay.weather.api_objects.ApiResponseData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TomorrowMiner extends AbstractMiner {

    public TomorrowMiner(String name, String url, String apiKey) {
        super(name, url, apiKey);
    }

    @Override
    public WeatherSample processResponse(String response, Location location) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ApiResponseData responseData = objectMapper.readValue(response, ApiResponse.class).getData();
        return WeatherSample.builder()
                .source("tomorrow-weather")
                .time(responseData.getTime())
                .location(location)
                .temperature(responseData.getValues().getTemperature())
                .humidity(responseData.getValues().getHumidity())
                .build();

    }
}
