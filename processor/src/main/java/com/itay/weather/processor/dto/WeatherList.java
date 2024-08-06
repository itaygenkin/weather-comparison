package com.itay.weather.processor.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class WeatherList {
    private String source;
    private Location location;
    private List<WeatherSample> samples = new ArrayList<>();

    public WeatherList(String source, Location location) {
        this.source = source;
        this.location = location;
    }

    public WeatherList(String source, Location location, List<WeatherSample> samplesList) {
        this.source = source;
        this.location = location;
        // insert only the samples that have same source and location
        for (WeatherSample weatherSample : samplesList) {
            if (weatherSample.getSource().equals(source) && weatherSample.getLocation().equals(location))
                this.samples.add(weatherSample);
        }
    }

    public void addSample(WeatherSample weatherSample) {
        if (weatherSample.getSource().equals(source) && weatherSample.getLocation().equals(location))
            this.samples.add(weatherSample);
    }
}
