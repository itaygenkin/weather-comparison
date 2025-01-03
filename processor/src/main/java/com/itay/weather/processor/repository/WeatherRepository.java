package com.itay.weather.processor.repository;

import com.itay.weather.dto.Location;
import com.itay.weather.processor.model.WeatherSampleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherSampleModel, Long> {
    // TODO: test methods
    List<WeatherSampleModel> findAllByLocation(Location location);
    List<WeatherSampleModel> findAllByLocationAndTimeBetween(Location location, Timestamp from, Timestamp to);
}
