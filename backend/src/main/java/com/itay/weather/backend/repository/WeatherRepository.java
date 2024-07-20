package com.itay.weather.backend.repository;

import com.itay.weather.backend.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherData, Long> {
}