package com.itay.weather.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataDto {
    private String source;
    private String location;
    private LocalDateTime time;
    private Double temperature;
    private Double humidity;
}
