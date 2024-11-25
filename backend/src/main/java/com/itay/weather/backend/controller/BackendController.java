package com.itay.weather.backend.controller;

import com.itay.weather.backend.service.BackendService;
import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam String country,
            @RequestParam String start,
            @RequestParam String end
    ) {
        log.info("GET request: city({}), country({}), from({}), to({})", city, country, start, end);

        Location location = new Location(city, country);
        WeatherPacket weatherPacket = backendService.getWeatherData(location, start, end);
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