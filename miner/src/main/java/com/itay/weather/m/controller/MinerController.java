package com.itay.weather.miner.controller;

import com.itay.weather.miner.service.MinerService;
import com.itay.weather.dto.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class MinerController {

    private final MinerService minerService;

    public MinerController(MinerService minerService) {
        this.minerService = minerService;
    }

    @PostMapping("/fetch")
    public ResponseEntity<Void> fetchData(@RequestBody Location location) {
        log.info("fetching data from location '{}'", location);
        minerService.fetchAndSendData(location);
        return ResponseEntity.ok().build();
    }

}
