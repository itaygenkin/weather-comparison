package com.itay.weather.processor.model;

import com.itay.weather.dto.Location;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Entity
public class LocationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;

    public boolean equals(Location location){
        return this.city.equals(location.getCity()) && this.country.equals(location.getCountry());
    }

    public Location toLocationDto() {
        return Location.builder()
                .city(this.city)
                .country(this.country)
                .longitude(this.longitude)
                .longitude(this.longitude)
                .build();
    }
}
