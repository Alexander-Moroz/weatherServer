package weather;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.Future;

@Controller
public class WeatherController {
    private static final Logger LOG = Logger.getLogger(WeatherController.class);

    @Autowired
    WeatherService weatherService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(
            @RequestParam(value = "cityId", required = false, defaultValue = "") String cityId,
            @RequestParam(value = "cityName", required = false, defaultValue = "") String cityName,
            @RequestParam(value = "latitude", required = false, defaultValue = "") String latitude,
            @RequestParam(value = "longitude", required = false, defaultValue = "") String longitude,
            Model model) {

        Future<String> weather;

        LOG.info("SEARCH WITH : { id=" + cityId + ", cityName=" + cityName + ", lon=" + longitude + ", lat=" + latitude + "}");

        // ALL PARAMS CHECK
        if ((cityId == null || cityId.isEmpty()) && (cityName == null || cityName.isEmpty()) && ((latitude == null || latitude.isEmpty()) && (longitude == null || longitude.isEmpty()))) {
            String errorString = "All params are empty";
            LOG.warn(errorString);
            model.addAttribute("errorString", errorString);

            return "exceptionPage";
        }

        // CITY ID CHECK
        try {
            if (cityId != null && !cityId.isEmpty() && Integer.parseInt(cityId) > 0) {
                weather = weatherService.getWeatherForCityId(cityId);
                LOG.debug("Search for city id: " + cityId);
                while (!weather.isDone()) {
                    Thread.sleep(10);
                }
                if (weather.get().isEmpty()) {
                    String errorString = "Weather for cityId = " + cityId + " not found";
                    model.addAttribute("errorString", errorString);
                    LOG.warn(errorString);
                    return "exceptionPage";
                }
                model.addAttribute("cityId", cityId);
                model.addAttribute("weather", weather.get());

                return "search";
            }
        } catch (NumberFormatException e) {
            String errorString = "invalid param cityId = " + cityId;
            LOG.warn(errorString);
            model.addAttribute("errorString", errorString);
            return "exceptionPage";
        } catch (Exception e) {
            LOG.error(e.getMessage());
            model.addAttribute("errorString", e.getMessage());
            return "exceptionPage";
        }

        // CITY NAME CHECK
        try {
            if (cityName != null && !cityName.isEmpty()) {
                weather = weatherService.getWeatherForCityName(cityName);
                LOG.debug("Search for city name: " + cityName);
                while (!weather.isDone()) {
                    Thread.sleep(10);
                }
                if (weather.get().isEmpty()) {
                    String errorString = "Weather for cityName = " + cityName + " not found";
                    model.addAttribute("errorString", errorString);
                    LOG.warn(errorString);
                    return "exceptionPage";
                }
                model.addAttribute("cityName", cityName);
                model.addAttribute("weather", weather.get());

                return "search";
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            model.addAttribute("errorString", e.getMessage());
            return "exceptionPage";
        }

        // COORDS CHECK
        try {
            if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty() && Double.parseDouble(latitude) > 0 && Double.parseDouble(longitude) > 0) {
                weather = weatherService.getWeatherForCoordinates(Double.parseDouble(latitude), Double.parseDouble(longitude));
                LOG.debug("Search for coords: lat=" + latitude + ", lon=" + longitude);
                while (!weather.isDone()) {
                    Thread.sleep(10);
                }
                if (weather.get().isEmpty()) {
                    String errorString = "Weather for coords: lon=" + longitude + " & lat=" + latitude + " not found";
                    model.addAttribute("errorString", errorString);
                    LOG.warn(errorString);
                    return "exceptionPage";
                }
                model.addAttribute("latitude", latitude);
                model.addAttribute("longitude", longitude);
                model.addAttribute("weather", weather.get());

                return "search";
            }
        } catch (NumberFormatException e) {
            String errorString = "invalid params for lat= " + latitude + ", lon=" + longitude;
            LOG.warn(errorString);
            model.addAttribute("errorString", errorString);
            return "exceptionPage";
        } catch (Exception e) {
            LOG.error(e.getMessage());
            model.addAttribute("errorString", e.getMessage());
            return "exceptionPage";
        }

        // DEFAULT
        try {
            weather = weatherService.getDefaultWeather();
            LOG.debug("DEFAULT search");
            while (!weather.isDone()) {
                Thread.sleep(10);
            }
            model.addAttribute("cityId", cityId);
            model.addAttribute("cityName", cityName);
            model.addAttribute("latitude", latitude);
            model.addAttribute("longitude", longitude);
            model.addAttribute("weather", weather.get());

            return "search";
        } catch (Exception e) {
            LOG.error(e.getMessage());
            model.addAttribute("errorString", e.getMessage());
            return "exceptionPage";
        }
    }
}