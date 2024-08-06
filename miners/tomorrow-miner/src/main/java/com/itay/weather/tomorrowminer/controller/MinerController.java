package com.itay.weather.tomorrowminer.controller;

import com.itay.weather.tomorrowminer.dto.Location;
import com.itay.weather.tomorrowminer.dto.WeatherSample;
import com.itay.weather.tomorrowminer.producer.MinerProducer;
import com.itay.weather.tomorrowminer.service.MinerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/tomorrow")
@Slf4j
public class MinerController {

    private final MinerService minerService;
    private final MinerProducer minerProducer;

    public MinerController(MinerService minerService, MinerProducer minerProducer) {
        this.minerService = minerService;
        this.minerProducer = minerProducer;
    }

    @GetMapping
    public ResponseEntity<WeatherSample> fetchData(@RequestBody Optional<Location> location) {
        log.info("Fetching data");
        Location loc = new Location("tel-aviv", "israel", 32.109, 34.855);
        try {
            WeatherSample data = minerService.fetchAndSendData(loc);
//            minerProducer.sendDataToKafka(data);
            return ResponseEntity.ok(data);
        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
