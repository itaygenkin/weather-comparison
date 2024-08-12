package com.itay.weather.backend.controller;

import com.itay.weather.backend.dto.Location;
import com.itay.weather.backend.dto.WeatherPacket;
import com.itay.weather.backend.service.BackendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BackendController {

    private final BackendService backendService;

    public BackendController(BackendService backendService) {
        this.backendService = backendService;
    }

    @GetMapping("/weather-data")
    public ResponseEntity<WeatherPacket> getWeatherData(@RequestBody Location location) {
        WeatherPacket weatherPacket = backendService.getWeatherData(location);
//        WeatherPacket wp = new WeatherPacket();
        return ResponseEntity.ok(weatherPacket);
    }

    @PostMapping("/trigger")
    public ResponseEntity<Void> triggerMiners(){
        if (backendService.trigger())
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

}