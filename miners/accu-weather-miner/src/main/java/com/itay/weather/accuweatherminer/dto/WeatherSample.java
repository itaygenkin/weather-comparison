package com.itay.weather.accuweatherminer.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherSample {
    private String source;
    private Location location;
    private Timestamp time;
    private Double temperature;
    private Integer humidity;

    public boolean isInTime(Timestamp start, Timestamp end) {
        return time.compareTo(start) >= 0 && time.compareTo(end) <= 0;
    }
}
