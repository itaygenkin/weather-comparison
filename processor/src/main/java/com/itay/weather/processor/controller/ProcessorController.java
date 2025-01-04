package com.itay.weather.processor.controller;

import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherPacket;
import com.itay.weather.processor.service.ProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

//        WeatherPacket data = processorService.getWeatherDataByLocationAndTime(
//                location, LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter)
//        );
        WeatherPacket data = processorService.getWeatherDataByLocation(location);

        System.out.println(data.getList3().getSamples().size());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
