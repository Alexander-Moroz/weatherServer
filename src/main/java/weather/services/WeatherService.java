package weather.services;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import weather.controllers.WeatherController;

import java.util.concurrent.CompletableFuture;

@Service
public class WeatherService {
    private static final Logger LOG = Logger.getLogger(WeatherController.class);
    private static OWM owm = new OWM("07cb39d0d64307ac8862d0136a00bd0e");
    private static final int LATITUDE_MIN = -90;
    private static final int LATITUDE_MAX = 90;
    private static final int LONGITUDE_MIN = -180;
    private static final int LONGITUDE_MAX = 180;

    @Async("weatherSiteApiExecutor")
    public CompletableFuture<CurrentWeather> getWeather(String cityId, String cityName, double latitude, double longitude) {
        LOG.debug(Thread.currentThread().getName() + " starts get weather from root service async");
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather weather = null;

        try {
            if (!StringUtils.isEmpty(cityId)) {
                weather = owm.currentWeatherByCityId(Integer.parseInt(cityId));
            } else if (!StringUtils.isEmpty(cityName)) {
                weather = owm.currentWeatherByCityName(cityName);
            } else if (latitude < LATITUDE_MAX && latitude > LATITUDE_MIN && longitude < LONGITUDE_MAX && longitude > LONGITUDE_MIN) {
                weather = owm.currentWeatherByCoords(latitude, longitude);
            } else {
                throw new RuntimeException("Not valid input");
            }
        } catch (APIException e) {
            LOG.error("THROWN API Exception: " + e.getMessage());
        } catch (Exception e) {
            LOG.warn(e.getMessage());
        }

        LOG.debug(Thread.currentThread().getName() + " ends get weather from root service async");

        return CompletableFuture.completedFuture(weather);
    }
}
