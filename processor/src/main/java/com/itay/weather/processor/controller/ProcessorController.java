package com.itay.weather.processor.controller;

import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherPacket;
import com.itay.weather.processor.service.ProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
            @RequestParam(value = "country") String country,
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to
    ){
        log.info("function call: 'getWeatherData'");
        log.info("params: city({}), country({}), from({}), to({})", city, country, from, to);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        Location location = new Location(city, country);
        LocalDateTime fromTime;
        LocalDateTime toTime;

        try {
            fromTime = LocalDateTime.parse(from, formatter);
            toTime = LocalDateTime.parse(to, formatter);
        } catch (DateTimeParseException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        WeatherPacket data = processorService.getWeatherDataByLocationAndTime(location, fromTime, toTime);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
