package com.itay.weather.miner.objects;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MinerValues {
    @Value("${TOMORROW_WEATHER_API_KEY}")
    private final String tomorrowApiKey;
    @Value("${TOMORROW_WEATHER_API_URL}")
    private final String tomorrowApiUrl;
    @Value("${OPEN_WEATHER_API_KEY}")
    private final String openWeatherApiKey;
    @Value("${OPEN_WEATHER_API_URL}")
    private final String OpenWeatherApiUrl;
    @Value("${ACCU_WEATHER_API_KEY}")
    private final String accuWeatherApiKey;
    @Value("${ACCU_WEATHER_API_URL}")
    private final String accuWeatherApiUrl;

    public MinerValues() {
        this.tomorrowApiKey = System.getProperty("TOMORROW_WEATHER_API_KEY");
        this.tomorrowApiUrl = System.getProperty("TOMORROW_WEATHER_API_URL");
        this.openWeatherApiKey = System.getProperty("OPEN_WEATHER_API_KEY");
        this.OpenWeatherApiUrl = System.getProperty("OPEN_WEATHER_API_URL");
        this.accuWeatherApiKey = System.getProperty("ACCU_WEATHER_API_KEY");
        this.accuWeatherApiUrl = System.getProperty("ACCU_WEATHER_API_URL");
    }
}
