package com.itay.weather.openweatherminer.controller;

import com.itay.weather.openweatherminer.service.MinerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MinerController {

    private final MinerService minerService;

    public MinerController(MinerService minerService) {
        this.minerService = minerService;
    }

    @GetMapping
    public ResponseEntity<Void> fetchData() {
        try {
            minerService.fetchAndSendData();
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
