package weather;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.Future;

@Service
public class WeatherService {
    private static final Logger LOG = Logger.getLogger(WeatherController.class);
    private static OWM owm = new OWM("07cb39d0d64307ac8862d0136a00bd0e");

    @Async
    public Future<String> getWeatherForCityId(String cityId) throws APIException, InterruptedException {
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather currentWeather = owm.currentWeatherByCityId(Integer.parseInt(cityId));
        Thread.sleep(new Random().nextInt(5000));
        return new AsyncResult<String>("WEATHER FOR CITY ID(" + cityId + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now");
    }

    @Async
    public Future<String> getWeatherForCityName(String cityName) throws APIException, InterruptedException {
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather currentWeather = owm.currentWeatherByCityName(cityName);
        Thread.sleep(new Random().nextInt(5000));
        return new AsyncResult<String>("WEATHER FOR CITY NAME(" + cityName + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now");
    }

    @Async
    public Future<String> getWeatherForCoordinates(double latitude, double longitude) throws APIException, InterruptedException {
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather currentWeather = owm.currentWeatherByCoords(latitude, longitude);
        Thread.sleep(new Random().nextInt(5000));
        return new AsyncResult<>("WEATHER FOR COORDINATES(lat: " + latitude + ", lon: " + longitude + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now");
    }

    @Async
    public Future<String> getDefaultWeather() throws APIException, InterruptedException {
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather currentWeather = owm.currentWeatherByCityId(498817);
        Thread.sleep(new Random().nextInt(5000));
        return new AsyncResult<String>("NOT FOUND. Default: " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now");
    }
}
