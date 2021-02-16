package com.amoroz.weather.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.amoroz.weather.exceptions.WeatherException;
import com.amoroz.weather.model.SearchRequest;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final OWM owm;

    @Async("weatherSiteApiExecutor")
    @Override
    public CompletableFuture<CurrentWeather> getWeather(SearchRequest request) {
        if (request.getCityId() != null) {
            return getWeatherByCityId(request.getUserName(), request.getCityId());
        } else if (StringUtils.hasLength(request.getCityName())) {
            return getWeatherByCityName(request.getUserName(), request.getCityName());
        } else if (request.getLatitude() != null && request.getLongitude() != null) {
            return getWeatherByCityCoordinates(request.getUserName(), request.getLatitude(), request.getLongitude());
        } else {
            throw new WeatherException("Invalid input");
        }
    }

    private CompletableFuture<CurrentWeather> getWeatherByCityId(String userName, Integer cityId) {
        try {
            if (cityId > 0) {
                if (log.isDebugEnabled()) {
                    log.debug("<" + userName + "> Search for city id: " + cityId);
                }
                var weather = owm.currentWeatherByCityId(cityId);
                return CompletableFuture.completedFuture(weather);
            } else {
                throw new WeatherException("Invalid city id: " + cityId);
            }
        } catch (Exception e) {
            throw new WeatherException(e);
        }
    }

    private CompletableFuture<CurrentWeather> getWeatherByCityName(String userName, String cityName) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("<" + userName + "> Searching for city name: " + cityName);
            }
            var weather = owm.currentWeatherByCityName(cityName);
            return CompletableFuture.completedFuture(weather);
        } catch (Exception e) {
            throw new WeatherException(e);
        }
    }

    private CompletableFuture<CurrentWeather> getWeatherByCityCoordinates(String userName, Double latitude, Double longitude) {
        try {
            if (latitude < LATITUDE_MAX && latitude > LATITUDE_MIN && longitude < LONGITUDE_MAX && longitude > LONGITUDE_MIN) {
                if (log.isDebugEnabled()) {
                    log.debug("<" + userName + "> Search for coords: latitude=" + latitude + ", longitude=" + longitude);
                }
                var weather = owm.currentWeatherByCoords(latitude, longitude);
                return CompletableFuture.completedFuture(weather);
            } else {
                throw new WeatherException("Bad coords: lon=" + longitude + "(min -180, max 180) & lat=" + latitude + "(min -90, max 90)");
            }
        } catch (Exception e) {
            throw new WeatherException(e);
        }
    }
}