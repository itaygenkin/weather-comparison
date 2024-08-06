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
}
