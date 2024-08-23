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
    public ResponseEntity<WeatherPacket> getWeatherData(
            @RequestParam String city,
            @RequestParam String country
    ) {
        Location location = new Location(city, country);
        WeatherPacket weatherPacket = backendService.getWeatherData(location);
        if (weatherPacket == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(weatherPacket);
    }

    @PostMapping("/trigger")
    public ResponseEntity<Void> triggerMiners(){
        if (backendService.trigger())
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

}