package com.itay.weather.processor.repository;

import com.itay.weather.processor.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherData, Long> {
}
