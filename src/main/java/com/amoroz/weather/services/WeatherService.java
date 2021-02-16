package com.amoroz.weather.services;

import net.aksingh.owmjapis.model.CurrentWeather;
import org.springframework.scheduling.annotation.Async;
import com.amoroz.weather.model.SearchRequest;

import java.util.concurrent.CompletableFuture;

public interface WeatherService {

    int LATITUDE_MIN = -90;
    int LATITUDE_MAX = 90;
    int LONGITUDE_MIN = -180;
    int LONGITUDE_MAX = 180;

    @Async("weatherSiteApiExecutor")
    CompletableFuture<CurrentWeather> getWeather(SearchRequest searchRequest);
}
