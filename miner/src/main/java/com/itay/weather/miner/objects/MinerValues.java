package com.itay.weather.miner.objects;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class MinerValues {
    private static volatile MinerValues instance = null;

    private final String tomorrowApiKey;
    private final String tomorrowApiUrl;
    private final String openWeatherApiKey;
    private final String openWeatherApiUrl;
    private final String accuWeatherApiKey;
    private final String accuWeatherApiUrl;

    private MinerValues() {
        Dotenv dotenv = Dotenv.load();
        this.tomorrowApiKey = dotenv.get("TOMORROW_WEATHER_API_KEY");
        this.tomorrowApiUrl = dotenv.get("TOMORROW_WEATHER_API_URL");
        this.openWeatherApiKey = dotenv.get("OPEN_WEATHER_API_KEY");
        this.openWeatherApiUrl = dotenv.get("OPEN_WEATHER_API_URL");
        this.accuWeatherApiKey = dotenv.get("ACCU_WEATHER_API_KEY");
        this.accuWeatherApiUrl = dotenv.get("ACCU_WEATHER_API_URL");
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
