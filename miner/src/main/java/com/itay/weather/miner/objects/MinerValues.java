package com.itay.weather.miner.objects;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class MinerValues {
    private static volatile MinerValues instance = null;

    @Value("${TOMORROW_WEATHER_API_KEY}")
    private final String tomorrowApiKey;
    @Value("${TOMORROW_WEATHER_API_URL}")
    private final String tomorrowApiUrl;
    @Value("${OPEN_WEATHER_API_KEY}")
    private final String openWeatherApiKey;
    @Value("${OPEN_WEATHER_API_URL}")
    private final String openWeatherApiUrl;
    @Value("${ACCU_WEATHER_API_KEY}")
    private final String accuWeatherApiKey;
    @Value("${ACCU_WEATHER_API_URL}")
    private final String accuWeatherApiUrl;

    private MinerValues() {
        this.tomorrowApiKey = System.getProperty("TOMORROW_WEATHER_API_KEY");
        this.tomorrowApiUrl = System.getProperty("TOMORROW_WEATHER_API_URL");
        this.openWeatherApiKey = System.getProperty("OPEN_WEATHER_API_KEY");
        this.openWeatherApiUrl = System.getProperty("OPEN_WEATHER_API_URL");
        this.accuWeatherApiKey = System.getProperty("ACCU_WEATHER_API_KEY");
        this.accuWeatherApiUrl = System.getProperty("ACCU_WEATHER_API_URL");
    }

    public static MinerValues getInstance() {
        if (instance == null){
            synchronized (MinerValues.class){
                if (instance == null)
                    instance = new MinerValues();
            }
        }
        return instance;
    }
}
