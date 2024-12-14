package com.itay.weather.m.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class MinerConfig {

    private final String tomorrowApiUrl;
    private final String tomorrowApiKey;
    private final String openWeatherApiUrl;
    private final String openWeatherApiKey;
    private final String accuWeatherApiUrl;
    private final String accuWeatherApiKey;

    public MinerConfig() {
        Dotenv dotenv = Dotenv.load();
        this.tomorrowApiUrl = dotenv.get("TOMORROW_WEATHER_API_URL");
        this.tomorrowApiKey = dotenv.get("TOMORROW_WEATHER_API_KEY");
        this.openWeatherApiUrl = dotenv.get("OPEN_WEATHER_API_URL");
        this.openWeatherApiKey = dotenv.get("OPEN_WEATHER_API_KEY");
        this.accuWeatherApiUrl = dotenv.get("ACCU_WEATHER_API_URL");
        this.accuWeatherApiKey = dotenv.get("ACCU_WEATHER_API_KEY");
    }
}
