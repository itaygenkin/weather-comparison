package com.itay.weather.processor.controller;

import com.itay.weather.processor.dto.Location;
import com.itay.weather.processor.dto.WeatherPacket;
import com.itay.weather.processor.service.ProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
public class ProcessorController {

    private final ProcessorService processorService;

    @Autowired
    public ProcessorController(ProcessorService processorService) {
        this.processorService = processorService;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherPacket> getWeatherData(
            @RequestParam(value = "city") String city,
            @RequestParam(value = "country") String country
    ){
        Location location = new Location(city, country);
        WeatherPacket data = processorService.getAllWeatherData(location);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
