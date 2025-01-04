package com.itay.weather.processor.repository;

import com.itay.weather.dto.Location;
import com.itay.weather.processor.model.WeatherSampleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherSampleModel, Long> {
    // TODO: test methods
    List<WeatherSampleModel> findAllByLocation(Location location);
    List<WeatherSampleModel> findAllByLocationAndTimeBetween(Location location, Timestamp from, Timestamp to);
}
