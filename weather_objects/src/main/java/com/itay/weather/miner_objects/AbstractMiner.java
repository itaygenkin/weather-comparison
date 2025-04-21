package com.itay.weather.miner_objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherSample;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Data
@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractMiner {
    protected final String minerName;
    protected final String baseUrl;
    protected final String apiKey;

    public String buildUrl(HashMap<String, String> params) {
        StringBuilder url = new StringBuilder(baseUrl);
        params.put("apikey", apiKey);
        url.append("?");
        for (String key : params.keySet())
            url.append(key).append("=").append(params.get(key)).append("&");
        return url.substring(0, url.length() - 1);
    }

    public abstract WeatherSample processResponse(String response, Location location) throws JsonProcessingException, NullPointerException;
}
