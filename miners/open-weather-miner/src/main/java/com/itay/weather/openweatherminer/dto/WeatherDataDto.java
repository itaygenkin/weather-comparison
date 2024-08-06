package com.itay.weather.openweatherminer.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataDto {
    private String source;
    private String location;
    private Timestamp time;
    private Double temperature;
    private Double humidity;
}