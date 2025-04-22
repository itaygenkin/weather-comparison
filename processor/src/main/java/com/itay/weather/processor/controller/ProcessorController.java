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
            @RequestParam(value = "start") String start,
            @RequestParam(value = "end") String end
    ){
        log.info("function call: 'getWeatherData'");
        log.info("params: city({}), country({}), from({}), to({})", city, country, start, end);

        Location location = new Location(city, country);
        WeatherPacket data = processorService.getWeatherDataByLocationAndTime(location, start, end);

        if (data == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        WeatherPacket data = processorService.getWeatherDataByLocationAndTime(location, fromTime, toTime);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/weather/{locationId}")
    public ResponseEntity<WeatherPacket> getWeatherDataByLocationId(@PathVariable(value = "locationId") long locationId) {
        WeatherPacket weatherPacket = processorService.getWeatherDataByLocation(locationId);

        if (weatherPacket == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(weatherPacket, HttpStatus.OK);
    }

    @PostMapping("/addLocation")
    public ResponseEntity<Void> addLocation(@RequestBody Location location) {
        log.info("function call: 'addLocation', params({})", location);

        if (processorService.addLocation(location))
            return new ResponseEntity<>(HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<LocationModel>> getCities() {
        log.info("function call: 'getCities'");

        List<LocationModel> locations = processorService.getLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
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
