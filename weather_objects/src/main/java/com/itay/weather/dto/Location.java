package com.itay.weather.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.HashMap;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Location {
    private String city;
    private String country;
    private double latitude = 0.0;
    private double longitude = 0.0;

    public Location(String city, String country){
        this.city = city;
        this.country = country;
    }

    public String toStringByDegrees(){
        return String.format("location=%f,%f", latitude, longitude);
    }

    public String toString(){
        if (city == null || country == null)
            return "location=NULL";
//        return String.format("location=%s-%s", city, country);
        return String.format("%s-%s", city, country);
    }

    public boolean equals(Location location){
        if (city == null || country == null)
            return false;
        else if (latitude != 0.0 && longitude != 0.0)
            return euclideanDistance(location) < 1;
        else
            return (this.city.equals(location.getCity()) && this.country.equals(location.getCountry()));
    }

    private double euclideanDistance(Location location) {
        return Math.sqrt(Math.pow(location.getLatitude() - latitude, 2)
                + Math.pow(location.getLongitude() - longitude, 2));
    }

    public HashMap<String, String> toHashMap(){
        HashMap<String, String> map = new HashMap<>();
        map.put("city", this.city);
        map.put("country", this.country);
        return map;
    }
}
