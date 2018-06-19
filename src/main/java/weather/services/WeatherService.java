package weather.services;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import weather.exceptions.WeatherException;

import java.util.concurrent.CompletableFuture;

@Service
public class WeatherService {
    private static final Logger LOG = Logger.getLogger(WeatherService.class);
    private static String APPID = "07cb39d0d64307ac8862d0136a00bd0e";
    private static OWM owm = new OWM(APPID);
    private static final int LATITUDE_MIN = -90;
    private static final int LATITUDE_MAX = 90;
    private static final int LONGITUDE_MIN = -180;
    private static final int LONGITUDE_MAX = 180;

    @Async("weatherSiteApiExecutor")
    public CompletableFuture<CurrentWeather> getWeather(String userName, String cityId, String cityName, String latitude, String longitude) throws WeatherException {
        owm  = new OWM(APPID);
        LOG.debug(Thread.currentThread().getName() + " starts get weather from root service async");
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather weather = null;

        // ALL PARAMS CHECK
        if ((StringUtils.isEmpty(cityId)) && (StringUtils.isEmpty(cityName)) && (StringUtils.isEmpty(latitude) && (StringUtils.isEmpty(longitude)))) {
            String errorString = "All params are empty";
            throw new WeatherException(errorString);
        }

        if (!StringUtils.isEmpty(cityId)) {
            // CITY ID CHECK
            try {
                if (!StringUtils.isEmpty(cityId) && Integer.parseInt(cityId) > 0) {
                    LOG.debug("<" + userName + "> Search for city id: " + cityId);
                    weather = owm.currentWeatherByCityId(Integer.parseInt(cityId));
                    if (weather == null) {
                        throw new WeatherException("Weather for cityId = " + cityId + " not found");
                    }
                    return CompletableFuture.completedFuture(weather);
                }
            } catch (NumberFormatException e) {
                throw new WeatherException("invalid param cityId = " + cityId);
            } catch (Exception e) {
                throw new WeatherException(e.getMessage());
            }
        } else if (!StringUtils.isEmpty(cityName)) {
            // CITY NAME CHECK
            try {
                if (!StringUtils.isEmpty(cityName)) {
                    LOG.debug("<" + userName + "> Search for city name: " + cityName);
                    weather = owm.currentWeatherByCityName(cityName);
                    if (weather == null) {
                        throw new WeatherException("Weather for cityName = " + cityName + " not found");
                    }
                    return CompletableFuture.completedFuture(weather);
                }
            } catch (Exception e) {
                throw new WeatherException(e.getMessage());
            }
        } else if (Double.parseDouble(latitude) < LATITUDE_MAX && Double.parseDouble(latitude) > LATITUDE_MIN && Double.parseDouble(longitude) < LONGITUDE_MAX && Double.parseDouble(longitude) > LONGITUDE_MIN) {
            // COORDS CHECK
            try {
                if (!StringUtils.isEmpty(latitude) && !StringUtils.isEmpty(longitude)) {
                    LOG.debug("<" + userName + "> Search for coords: latitude=" + latitude + ", longitude=" + longitude);
                    if (Double.parseDouble(latitude) < LATITUDE_MIN || Double.parseDouble(latitude) > LATITUDE_MAX || Double.parseDouble(longitude) < LONGITUDE_MIN || Double.parseDouble(longitude) > LONGITUDE_MAX) {
                        throw new WeatherException("Bad coords: lon=" + longitude + "(min -180, max 180) & lat=" + latitude + "(min -90, max 90)");
                    }
                    weather = owm.currentWeatherByCoords(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    if (weather == null) {
                        throw new WeatherException("Weather for coords: lon=" + longitude + " & lat=" + latitude + " not found");
                    }
                    return CompletableFuture.completedFuture(weather);
                } else if ((StringUtils.isEmpty(latitude) && !StringUtils.isEmpty(longitude)) || (StringUtils.isEmpty(longitude) && !StringUtils.isEmpty(latitude))) {
                    throw new NumberFormatException("invalid params for lat= " + latitude + ", lon=" + longitude);
                }
            } catch (NumberFormatException e) {
                throw new WeatherException(e.getMessage());
            } catch (Exception e) {
                throw new WeatherException(e.getMessage());
            }
        } else {
            throw new WeatherException("Not valid input");
        }

        LOG.debug(Thread.currentThread().getName() + " ends get weather from root service async");

        return CompletableFuture.completedFuture(weather);
    }
}