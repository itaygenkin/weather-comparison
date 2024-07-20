package com.itay.weather.processor.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataDto {
    private String source;
    private LocalDateTime time;
    private Float temperature;
    private Float humidity;
}
