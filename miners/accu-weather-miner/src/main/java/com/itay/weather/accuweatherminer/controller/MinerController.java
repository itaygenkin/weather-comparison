package com.itay.weather.accuweatherminer.controller;

import com.itay.weather.accuweatherminer.dto.Location;
import com.itay.weather.accuweatherminer.dto.WeatherSample;
import com.itay.weather.accuweatherminer.producer.MinerProducer;
import com.itay.weather.accuweatherminer.service.MinerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/accu-weather")
public class MinerController {

    private final MinerService minerService;
    private final MinerProducer minerProducer;

    public MinerController(MinerService minerService, MinerProducer minerProducer) {
        this.minerService = minerService;
        this.minerProducer = minerProducer;
    }

    @GetMapping
    public ResponseEntity<WeatherSample> fetchData(@RequestBody Optional<Location> location) {
        Location loc = new Location("tel-aviv", "israel", 32.109, 34.855);
        try {
            WeatherSample data = minerService.fetchAndSendData(loc);
            minerProducer.sendDataToKafka(data);
            return ResponseEntity.ok(data);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
