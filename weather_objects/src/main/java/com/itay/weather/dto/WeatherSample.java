package com.itay.weather.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
@JsonDeserialize
public class WeatherSample {

    private String source;
    private Location location;
    private LocalDateTime time;
    private Double temperature;
    private Integer humidity;

    public boolean isInTime(LocalDateTime start, LocalDateTime end) {
        return time.isAfter(start) && time.isBefore(end);
    }
}
