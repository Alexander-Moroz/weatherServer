package weather;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    private static final Logger LOG = Logger.getLogger(WeatherController.class);
    private static OWM owm = new OWM("07cb39d0d64307ac8862d0136a00bd0e");

    public String getWeatherForCityId(String cityId) throws APIException {
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather currentWeather = owm.currentWeatherByCityId(Integer.parseInt(cityId));

        return "WEATHER FOR CITY ID(" + cityId + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
    }

    public String getWeatherForCityName(String cityName) throws APIException {
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather currentWeather = owm.currentWeatherByCityName(cityName);

        return "WEATHER FOR CITY NAME(" + cityName + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
    }

    public String getWeatherForCoordinates(double latitude, double longitude) throws APIException {
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather currentWeather = owm.currentWeatherByCoords(latitude, longitude);

        return "WEATHER FOR COORDINATES(lat: " + latitude + ", lon: " + longitude + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
    }
}
