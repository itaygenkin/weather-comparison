package com.itay.weather.miner.controller;

import com.itay.weather.miner.service.MinerService;
import dto.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/miner")
public class MinerController {

    private final MinerService minerService;

    public MinerController(MinerService minerService) {
        this.minerService = minerService;
    }

    @PostMapping("/fetch")
    public ResponseEntity<Void> fetchData(@RequestBody Location location) {
        log.info("fetching data from location '{}'", location);
        try {
            minerService.fetchAndSendData(location);
        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
