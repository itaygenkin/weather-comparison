package com.itay.weather.processor.controller;

import com.itay.weather.processor.dto.Location;
import com.itay.weather.processor.dto.WeatherPacket;
import com.itay.weather.processor.service.ProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProcessorController {

    private final ProcessorService processorService;

    @Autowired
    public ProcessorController(ProcessorService processorService) {
        this.processorService = processorService;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherPacket> getWeatherData(@RequestBody Location location){
        WeatherPacket data = processorService.getAllWeatherData(location);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
