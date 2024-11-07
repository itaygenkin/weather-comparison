package com.itay.weather.backend.controller;

import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherList;
import com.itay.weather.dto.WeatherPacket;
import com.itay.weather.dto.WeatherSample;
import com.itay.weather.backend.service.BackendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
@Slf4j
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
        log.info("GET request: city_param: '{}', country_param: '{}'", city, country);

        Location location = new Location(city, country);
        WeatherPacket weatherPacket = backendService.getWeatherData(location);
        if (weatherPacket == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(weatherPacket);
    }

    @PostMapping("/trigger")
    public ResponseEntity<Void> triggerMiners(@RequestBody Location location){
        log.info("GET request: location_param: '{}'", location);
        if (backendService.trigger(location))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

}