package com.itay.weather.backend.controller;

import com.itay.weather.backend.service.BackendService;
import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/trigger")
    public ResponseEntity<Void> triggerMiners(@RequestBody Location location){
        log.info("PUT request: location_param: '{}'", location);

        if (backendService.trigger(location))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/addLocation")
    public ResponseEntity<Void> addLocation(@RequestBody Location location) {
        log.info("POST request: location_param: '{}'", location);
        if (backendService.addLocation(location))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/cities")
    public List<Location> getCities() {
        log.info("GET request: cities");
        return backendService.getCities();
    }
}