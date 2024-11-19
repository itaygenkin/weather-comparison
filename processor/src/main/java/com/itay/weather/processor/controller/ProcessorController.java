package com.itay.weather.processor.controller;

import com.itay.weather.processor.service.ProcessorService;
import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
@Slf4j
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
        log.info("function call: 'getWeatherData'");
        log.info("params: city({}), country({})", city, country);

        Location location = new Location(city, country);
        WeatherPacket data = processorService.getWeatherDataByLocation(location);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
