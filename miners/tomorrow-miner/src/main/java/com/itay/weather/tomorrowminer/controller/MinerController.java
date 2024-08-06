package com.itay.weather.tomorrowminer.controller;

import com.itay.weather.tomorrowminer.dto.Location;
import com.itay.weather.tomorrowminer.dto.WeatherDataDto;
import com.itay.weather.tomorrowminer.producer.MinerProducer;
import com.itay.weather.tomorrowminer.service.MinerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tomorrow")
public class MinerController {

    private final MinerService minerService;
    private final MinerProducer minerProducer;

    public MinerController(MinerService minerService, MinerProducer minerProducer) {
        this.minerService = minerService;
        this.minerProducer = minerProducer;
    }

    @GetMapping
    public ResponseEntity<WeatherDataDto> fetchData() {
        System.out.println("Fetching data");
        // TODO: get the location as an argument
        Location loc = new Location("tel-aviv", "israel", 32.109, 34.855);
        try {
            WeatherDataDto data = minerService.fetchAndSendData(loc);
//            minerProducer.sendDataToKafka(data);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
