package com.itay.weather.processor.model;

import com.itay.weather.processor.dto.Location;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherSampleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String source;
    @Embedded
    private Location location;
    private Timestamp time;
    private Double temperature;
    private Double humidity;
}
