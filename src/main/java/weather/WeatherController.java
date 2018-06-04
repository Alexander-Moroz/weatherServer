package weather;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {
    private static OWM owm = new OWM("07cb39d0d64307ac8862d0136a00bd0e");

    @RequestMapping("/search")
    public String search(
            @RequestParam(value = "cityId", required = false, defaultValue = "0") int cityId,
            @RequestParam(value = "cityName", required = false, defaultValue = "") String cityName,
            @RequestParam(value = "latitude", required = false, defaultValue = "0") double latitude,
            @RequestParam(value = "longitude", required = false, defaultValue = "0") double longitude,
            Model model) {

        model.addAttribute("cityId", cityId);
        model.addAttribute("cityName", cityName);
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);

        String s;

        if (cityId > 0) {
            s = getWeatherForCityId(cityId);
        } else if (cityName != null && !cityName.isEmpty()) {
            s = getWeatherForCityName(cityName);
        } else if (latitude > 0 && longitude > 0) {
            s = getWeatherForCoordinates(latitude, longitude);
        } else {
            s = "NOT FOUND. Default: " + getWeatherForCityId(498817);
        }

        model.addAttribute("weather", s);

        return "search";
    }

    public static String getWeatherForCityId(int cityId) {
        try {
            owm.setUnit(OWM.Unit.METRIC);
            CurrentWeather currentWeather = owm.currentWeatherByCityId(cityId);

            return "WEATHER FOR CITY ID(" + cityId + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
        } catch (APIException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getWeatherForCityName(String cityName) {
        try {
            owm.setUnit(OWM.Unit.METRIC);
            CurrentWeather currentWeather = owm.currentWeatherByCityName(cityName);

            return "WEATHER FOR CITY NAME(" + cityName + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
        } catch (APIException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getWeatherForCoordinates(double latitude, double longitude) {
        try {
            owm.setUnit(OWM.Unit.METRIC);
            CurrentWeather currentWeather = owm.currentWeatherByCoords(latitude, longitude);

            return "WEATHER FOR CITY COORDINATES(lat: " + latitude + ", lon: " + longitude + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
        } catch (APIException e) {
            e.printStackTrace();
        }

        return "";
    }
}