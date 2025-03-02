package com.itay.weather.miner_objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.api_objects.ApiResponse;
import com.itay.weather.api_objects.ApiResponseData;
import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherSample;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class TomorrowMiner extends AbstractMiner {

    public TomorrowMiner(String name, String url, String apiKey) {
        super(name, url, apiKey);
    }

    @Override
    public String buildUrl(HashMap<String, String> params) {
        return baseUrl + "?" +
                "apikey" + '=' + apiKey +
                "&location=" + params.get("city");
    }

    @Override
    public WeatherSample processResponse(String response, Location location) throws JsonProcessingException, NullPointerException {
        if (response == null)
            throw new NullPointerException("response json is null");

        ObjectMapper objectMapper = new ObjectMapper();
        ApiResponseData responseData = objectMapper.readValue(response, ApiResponse.class).getData();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.parse(responseData.getTime().toString().substring(0, 16), formatter);

        return WeatherSample.builder()
                .source(minerName)
                .time(time)
                .location(location)
                .temperature(responseData.getValues().getTemperature())
                .humidity(responseData.getValues().getHumidity())
                .build();
    }
}
