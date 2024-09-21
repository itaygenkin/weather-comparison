package com.itay.weather.processor.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LocationModel {
    private String city;
    private String country;
    private double latitude;
    private double longitude;

    public boolean equals(Location location){
        if (this.city.equals(location.getCity()) && this.country.equals(location.getCountry()))
            return true;
        return Math.abs(this.latitude - location.getLatitude()) < 0.1 &&
                Math.abs(this.longitude - location.getLongitude()) < 0.1;
    }
}
