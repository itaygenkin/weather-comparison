package com.itay.weather.accuweatherminer;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AccuWeatherMinerApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                        .directory(".")
                        .load();
        System.setProperty("ACCU_WEATHER_API_KEY", dotenv.get("ACCU_WEATHER_API_KEY"));
        System.setProperty("ACCU_WEATHER_API_URL", dotenv.get("ACCU_WEATHER_API_URL"));
        SpringApplication.run(AccuWeatherMinerApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
