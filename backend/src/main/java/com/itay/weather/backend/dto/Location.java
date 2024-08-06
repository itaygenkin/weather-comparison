package com.itay.weather.backend.dto;

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

    public boolean equals(Location location){
        if (this.city.equals(location.getCity()) && this.country.equals(location.getCountry()))
            return true;
        return Math.abs(this.latitude - location.getLatitude()) < 0.1 &&
                Math.abs(this.longitude - location.getLongitude()) < 0.1;
    }
}
