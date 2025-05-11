package com.itay.weather.processor.repository;

import com.itay.weather.dto.Location;
import com.itay.weather.processor.model.WeatherSampleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherSampleModel, Long> {

    List<WeatherSampleModel> findAllByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<WeatherSampleModel> getAllByLocation(Location location);
}
