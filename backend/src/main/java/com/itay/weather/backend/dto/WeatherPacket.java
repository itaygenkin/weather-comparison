package com.itay.weather.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherPacket {
    private WeatherList list1;
    private WeatherList list2;
    private WeatherList list3;
}
