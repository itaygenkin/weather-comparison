package com.itay.weather.accuweatherminer.controller;

import com.itay.weather.accuweatherminer.dto.WeatherDataDto;
import com.itay.weather.accuweatherminer.producer.MinerProducer;
import com.itay.weather.accuweatherminer.service.MinerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> fetchData() {
        System.out.println("Fetching data");
        try {
            WeatherDataDto data = minerService.fetchAndSendData();
            minerProducer.sendDataToKafka(data);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
