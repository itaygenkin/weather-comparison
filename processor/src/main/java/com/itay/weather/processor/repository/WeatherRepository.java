package com.itay.weather.processor.repository;

import com.itay.weather.processor.model.WeatherSampleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherSampleModel, Long> {
}
