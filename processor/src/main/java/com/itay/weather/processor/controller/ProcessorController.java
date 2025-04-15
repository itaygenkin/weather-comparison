package com.itay.weather.processor.controller;

import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherPacket;
import com.itay.weather.processor.model.LocationModel;
import com.itay.weather.processor.service.ProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

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

    @PostMapping("/addLocation")
    public ResponseEntity<Void> addLocation(@RequestBody Location location) {
        log.info("function call: 'addLocation', params({})", location);

        if (processorService.addLocation(location))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/cities")
    public List<LocationModel> getCities() {
        log.info("function call: 'getCities'");
        return processorService.getLocations();
    }

    @DeleteMapping("/cities")
    public ResponseEntity<Void> deleteCities() {
        log.info("function call: 'deleteCities'");
        processorService.deleteCities();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/city/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable long id) {
        log.info("function call: 'deleteCity', param({})", id);
        processorService.deleteCity(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
