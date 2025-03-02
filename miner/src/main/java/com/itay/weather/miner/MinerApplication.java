package com.itay.weather.miner;

import com.itay.weather.miner.component.MinerList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MinerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinerApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MinerList minerList() {
        return new MinerList();
    }
}
