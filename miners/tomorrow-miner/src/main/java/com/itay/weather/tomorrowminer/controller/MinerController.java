package com.itay.weather.tomorrowminer.controller;

import com.itay.weather.tomorrowminer.dto.Location;
import com.itay.weather.tomorrowminer.dto.WeatherSample;
import com.itay.weather.tomorrowminer.producer.MinerProducer;
import com.itay.weather.tomorrowminer.service.MinerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/miner/tomorrow")
@Slf4j
public class MinerController {

    private final MinerService minerService;
//    private final MinerProducer minerProducer;

    public MinerController(MinerService minerService/*, MinerProducer minerProducer*/) {
        this.minerService = minerService;
//        this.minerProducer = minerProducer;
    }

    @PostMapping
    public ResponseEntity<Void> fetchData(@RequestBody Location location) {
        log.info("Fetching data");
        Location loc = new Location("tel-aviv", "israel", 32.109, 34.855);
        try {
            if (!minerService.fetchAndSendData(location))
                return ResponseEntity.badRequest().build();

            minerProducer.sendDataToKafka(data);
            return ResponseEntity.ok(data);

        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
