package com.itay.weather.miner_objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherSample;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class OpenMiner extends AbstractMiner {

    public OpenMiner(String name, String url, String apiKey) {
        super(name, url, apiKey);
    }

    @Override
    public String buildUrl(HashMap<String, String> locationParams) {
        StringBuilder url = new StringBuilder(baseUrl);
        url.append("?");
        HashMap<String, String> params = buildParams(locationParams);
        for (String key : params.keySet())
            url.append(key).append("=").append(params.get(key)).append("&");
        return url.substring(0, url.length() - 1);
    }

    private HashMap<String, String> buildParams(HashMap<String, String> locationParams) {
        HashMap<String, String> params = new HashMap<>();
        params.put("appid", apiKey);
        params.put("units", "metric");
        params.put("q", locationParams.get("city"));
        return params;
    }

    public WeatherSample processResponse(String jsonResponse, Location location) throws JsonProcessingException, NullPointerException {
        if (jsonResponse == null)
            throw new NullPointerException("response is null");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode mainNode = objectMapper.readTree(jsonResponse).get("main");
        System.out.println(jsonResponse);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String now = LocalDateTime.now().format(formatter);
        LocalDateTime time = LocalDateTime.parse(now, formatter);

        return WeatherSample.builder()
                .source(minerName)
                .time(time)
                .location(location)
                .temperature(mainNode.get("temp").asDouble())
                .humidity(mainNode.get("humidity").asInt())
                .build();
    }
}
