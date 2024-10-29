package com.itay.weather.processor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
