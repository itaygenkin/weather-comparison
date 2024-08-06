package com.itay.weather.openweatherminer.controller;

import com.itay.weather.openweatherminer.dto.Location;
import com.itay.weather.openweatherminer.dto.WeatherSample;
import com.itay.weather.openweatherminer.producer.MinerProducer;
import com.itay.weather.openweatherminer.service.MinerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-weather")
public class MinerController {

    private final MinerService minerService;
    private final MinerProducer minerProducer;

    public MinerController(MinerService minerService, MinerProducer minerProducer) {
        this.minerService = minerService;
        this.minerProducer = minerProducer;
    }

    @GetMapping
    public ResponseEntity<WeatherSample> fetchData(@RequestBody Location location) {
        try {
            WeatherSample data = minerService.fetchAndSendData(location);
            minerProducer.sendDataToKafka(data);
            return ResponseEntity.ok(data);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
