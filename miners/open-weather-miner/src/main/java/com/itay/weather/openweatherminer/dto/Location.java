package com.itay.weather.openweatherminer.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private String city;
    private String country;
    private double latitude;
    private double longitude;

    public String toStringByDegrees(){
        return String.format("location=%f,%f", latitude, longitude);
    }

    public String toString(){
        return String.format("location=%s-%s", city, country);
    }
}
