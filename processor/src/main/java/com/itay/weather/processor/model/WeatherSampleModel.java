package com.itay.weather.processor.model;

import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherSample;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherSampleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String source;
    @Embedded
    private Location location;
    private LocalDateTime time;
    private Double temperature;
    private Integer humidity;

    public WeatherSampleModel(WeatherSample weatherSample){
        this.source = weatherSample.getSource();
        this.location = weatherSample.getLocation();
        this.time = weatherSample.getTime();
        this.temperature = weatherSample.getTemperature();
        this.humidity = weatherSample.getHumidity();
    }

    public boolean isInTime(LocalDateTime start, LocalDateTime end) {
        return time.isAfter(start) && time.isBefore(end);
    }

    public WeatherSample toWeatherSample(){
        return WeatherSample.builder()
                .source(source)
                .time(time)
                .location(location)
                .temperature(temperature)
                .humidity(humidity)
                .build();
    }
}
