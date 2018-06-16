package weather.services;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import weather.controllers.WeatherController;

import java.util.concurrent.CompletableFuture;

@Service
public class WeatherService {
    private static final Logger LOG = Logger.getLogger(WeatherController.class);
    private static OWM owm = new OWM("07cb39d0d64307ac8862d0136a00bd0e");

    @Async("weatherSiteApiExecutor")
    public CompletableFuture<CurrentWeather> getWeather(String cityId, String cityName, double latitude, double longitude) {
        LOG.debug(Thread.currentThread().getName() + " starts get weather from root service async");
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather weather = null;

        try {
            if (cityId != null && !cityId.isEmpty()) {
                weather = owm.currentWeatherByCityId(Integer.parseInt(cityId));
            } else if (cityName != null && !cityName.isEmpty()) {
                weather = owm.currentWeatherByCityName(cityName);
            } else if (latitude < 90 && latitude > -90 && longitude < 180 && longitude > -180) {
                weather = owm.currentWeatherByCoords(latitude, longitude);
            } else {
                throw new RuntimeException("Not valid input");
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage());
        }

        LOG.debug(Thread.currentThread().getName() + " ends get weather from root service async");

        return CompletableFuture.completedFuture(weather);
    }
}
