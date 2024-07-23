package com.itay.weather.accuweatherminer.controller;

import com.itay.weather.accuweatherminer.service.MinerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accu")
public class MinerController {

    private final MinerService minerService;

    public MinerController(MinerService minerService) {
        this.minerService = minerService;
    }

    @GetMapping
    public ResponseEntity<Void> fetchData() {
        System.out.println("Fetching data");
        try {
            minerService.fetchAndSendData();
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
