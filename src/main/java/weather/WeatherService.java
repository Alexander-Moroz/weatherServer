package weather;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.*;

@Service
public class WeatherService {
    private static final Logger LOG = Logger.getLogger(WeatherController.class);
    private static OWM owm = new OWM("07cb39d0d64307ac8862d0136a00bd0e");

    private static ExecutorService service = Executors.newFixedThreadPool(1);

    public String getWeatherForCityId(String cityId) throws InterruptedException {
        CurrentWeather currentWeather = getWeather(cityId, null, 0, 0);
        if (currentWeather != null) {
            return new String("WEATHER FOR CITY ID (" + cityId + "): \n" + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now");
        } else {
            return null;
        }
    }

    public String getWeatherForCityName(String cityName) throws InterruptedException {
        CurrentWeather currentWeather = getWeather(null, cityName, 0, 0);
        if (currentWeather != null) {
            return new String("WEATHER FOR CITY NAME (" + cityName + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now");
        } else {
            return null;
        }
    }

    public String getWeatherForCoordinates(double latitude, double longitude) throws InterruptedException {
        CurrentWeather currentWeather = getWeather(null, null, latitude, longitude);
        if (currentWeather != null) {
            return new String("WEATHER FOR COORDINATES (lat: " + latitude + ", lon: " + longitude + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now");
        } else {
            return null;
        }
    }

    public String getDefaultWeather() throws InterruptedException {
        CurrentWeather currentWeather = getWeather("498817", null, 0, 0);
        if (currentWeather != null) {
            return new String("NOT FOUND. Default: " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now");
        } else {
            return null;
        }
    }

    private CurrentWeather getWeather(String cityId, String cityName, double latitude, double longitude) throws InterruptedException {
        owm.setUnit(OWM.Unit.METRIC);

        Callable<CurrentWeather> callable = () -> {
            CurrentWeather weather = null;

            try {
                if (cityId != null && !cityId.isEmpty()) {
                    Thread.sleep(new Random().nextInt(5000));
                    return owm.currentWeatherByCityId(Integer.parseInt(cityId));
                } else if (cityName != null && !cityName.isEmpty()) {
                    Thread.sleep(new Random().nextInt(5000));
                    return owm.currentWeatherByCityName(cityName);
                } else if (latitude < 90 && latitude > -90 && longitude < 180 && longitude > -180) {
                    return owm.currentWeatherByCoords(latitude, longitude);
                } else {
                    throw new RuntimeException("Not valid input");
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }

            return weather;
        };

        Future<CurrentWeather> future = service.submit(callable);

        while (!future.isDone() && !future.isCancelled()) {
            Thread.sleep(500);
        }

        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
