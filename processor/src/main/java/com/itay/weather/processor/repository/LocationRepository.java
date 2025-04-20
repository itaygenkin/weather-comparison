package com.itay.weather.processor.repository;

import com.itay.weather.processor.model.LocationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<LocationModel, Long> {

}
