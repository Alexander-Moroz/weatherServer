package weather;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {
    private static final Logger LOG = Logger.getLogger(WeatherController.class);
    private static OWM owm = new OWM("07cb39d0d64307ac8862d0136a00bd0e");

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(
            @RequestParam(value = "cityId", required = false, defaultValue = "") String cityId,
            @RequestParam(value = "cityName", required = false, defaultValue = "") String cityName,
            @RequestParam(value = "latitude", required = false, defaultValue = "") String latitude,
            @RequestParam(value = "longitude", required = false, defaultValue = "") String longitude,
            Model model) {

        String weather;

        // ALL PARAMS CHECK
        if ((cityId == null || cityId.isEmpty()) && (cityName == null || cityName.isEmpty()) && ((latitude == null || latitude.isEmpty()) && (longitude == null || longitude.isEmpty()))) {
            String errorString = "All params are empty";
            LOG.error(errorString, new RuntimeException());
            model.addAttribute("errorString", errorString);
            return "exceptionPage";
        }

        // CITY ID CHECK
        try {
            if (cityId != null && !cityId.isEmpty() && Integer.parseInt(cityId) > 0) {
                weather = getWeatherForCityId(cityId);
                model.addAttribute("cityId", cityId);
                model.addAttribute("weather", weather);
                LOG.info("Search for city id: " + cityId);
                return "search";
            } else {
                LOG.info("City id is empty");
            }
        } catch (NumberFormatException e) {
            String errorString = "invalid param cityId = " + cityId;
            LOG.error(errorString);
            model.addAttribute("errorString", errorString);
            return "exceptionPage";
        }

        // CITY NAME CHECK
        if (cityName != null && !cityName.isEmpty()) {
            weather = getWeatherForCityName(cityName);
            model.addAttribute("cityName", cityName);
            model.addAttribute("weather", weather);
            LOG.info("Search for city name: " + cityName);
            return "search";
        } else {
            String errorString = "City name is empty";
            LOG.info(errorString);
        }

        // COORDS CHECK
        try {
            if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty() && Double.parseDouble(latitude) > 0 && Double.parseDouble(longitude) > 0) {
                weather = getWeatherForCoordinates(Double.parseDouble(latitude), Double.parseDouble(longitude));
                model.addAttribute("latitude", latitude);
                model.addAttribute("longitude", longitude);
                model.addAttribute("weather", weather);
                LOG.info("Search for coords: lat=" + latitude + ", lon=" + longitude);
                return "search";
            } else {
                LOG.info("latitude & longitude are empty");
            }
        } catch (NumberFormatException e) {
            String errorString = "invalid params for lat= " + latitude + ", lon=" + longitude;
            LOG.error(errorString);
            model.addAttribute("errorString", errorString);
            return "exceptionPage";
        }

        // DEFAULT
        weather = "NOT FOUND. Default: " + getWeatherForCityId("498817");
        model.addAttribute("weather", weather);
        LOG.info("DEFAULT search");
        return "search";
    }

    public static String getWeatherForCityId(String cityId) {
        try {
            owm.setUnit(OWM.Unit.METRIC);
            CurrentWeather currentWeather = owm.currentWeatherByCityId(Integer.parseInt(cityId));

            return "WEATHER FOR CITY ID(" + cityId + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
        } catch (APIException e) {
            LOG.error(e);
        }
        return "";
    }

    public static String getWeatherForCityName(String cityName) {
        try {
            owm.setUnit(OWM.Unit.METRIC);
            CurrentWeather currentWeather = owm.currentWeatherByCityName(cityName);

            return "WEATHER FOR CITY NAME(" + cityName + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
        } catch (APIException e) {
            LOG.error(e);
        }
        return "";
    }

    public static String getWeatherForCoordinates(double latitude, double longitude) {
        try {
            owm.setUnit(OWM.Unit.METRIC);
            CurrentWeather currentWeather = owm.currentWeatherByCoords(latitude, longitude);

            return "WEATHER FOR CITY COORDINATES(lat: " + latitude + ", lon: " + longitude + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
        } catch (APIException e) {
            LOG.error(e);
        }
        return "";
    }
}