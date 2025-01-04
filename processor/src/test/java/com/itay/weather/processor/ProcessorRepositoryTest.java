package com.itay.weather.processor;

import com.itay.weather.dto.Location;
import com.itay.weather.dto.WeatherSample;
import com.itay.weather.processor.model.WeatherSampleModel;
import com.itay.weather.processor.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class ProcessorRepositoryTest {

    @MockBean
    private WeatherRepository weatherRepository;
    private List<WeatherSample> weatherSampleList;
    private final List<WeatherSampleModel> weatherSampleModelList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        weatherRepository = mock(WeatherRepository.class);
        Location tlvLocation = new Location("tel-aviv", "israel");
        Location lisbonLocation = new Location("lisbon", "portugal");

        LocalDateTime time1 = LocalDateTime.of(2024, 12, 12, 10, 10);
        LocalDateTime time2 = LocalDateTime.of(2024, 12, 12, 10, 20);
        LocalDateTime time3 = LocalDateTime.of(2024, 12, 12, 10, 30);
        LocalDateTime time4 = LocalDateTime.of(2024, 12, 12, 10, 40);

        weatherSampleList = List.of(
                new WeatherSample("tomorrow-weather", tlvLocation, time1, 20.0, 65),
                new WeatherSample("tomorrow-weather", tlvLocation, time2, 20.5, 65),
                new WeatherSample("tomorrow-weather", tlvLocation, time3, 21.0, 60),
                new WeatherSample("tomorrow-weather", tlvLocation, time4, 22.0, 60),
                new WeatherSample("tomorrow-weather", lisbonLocation, time4, 12.0, 50)
        );
        for (WeatherSample ws : weatherSampleList) {
            weatherSampleModelList.add(new WeatherSampleModel(ws));
        }
    }

    @Test
    public void testGetWeatherData() {
        // Arrange
        when(weatherRepository.findAll()).thenReturn(weatherSampleModelList);

        // Act
        List<WeatherSampleModel> result = weatherRepository.findAll();

        // Assert
        assertEquals("", 5, weatherSampleList.size());
        assertEquals("Test result list size:", 5, result.size());
        assertTrue("Test result items:", result.containsAll(weatherSampleModelList));
    }

}
