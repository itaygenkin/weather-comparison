package com.itay.weather.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
@JsonDeserialize
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
