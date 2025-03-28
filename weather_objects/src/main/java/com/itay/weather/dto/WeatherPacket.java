package com.itay.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherPacket {
    private WeatherList list1;
    private WeatherList list2;
    private WeatherList list3;

    public WeatherPacket(List<WeatherSample> weatherSamples, Location location) {
        String[] sources = {"accu-weather", "open-weather", "tomorrow-weather"};
        this.list1 = new WeatherList(sources[0], location);
        this.list2 = new WeatherList(sources[1], location);
        this.list3 = new WeatherList(sources[2], location);

        // filter non-relevant samples
        weatherSamples = weatherSamples.stream()
                .filter(ws -> (ws != null) && (ws.getLocation() != null))
                .toList();

        for (WeatherSample weatherSample : weatherSamples){
            if (weatherSample.getSource().equals(sources[0]))
                this.list1.addSample(weatherSample);
            else if (weatherSample.getSource().equals(sources[1]))
                this.list2.addSample(weatherSample);
            else if (weatherSample.getSource().equals(sources[2]))
                this.list3.addSample(weatherSample);
        }
    }

    public WeatherPacket(List<WeatherSample> weatherSamples, Location location, LocalDateTime from, LocalDateTime to) {
        weatherSamples = weatherSamples.stream().filter(ws -> ws != null &&
                        !(ws.getTime() == null || ws.getTime().isBefore(from) || ws.getTime().isAfter(to)))
                .toList();
        this(weatherSamples, location);
    }

}
