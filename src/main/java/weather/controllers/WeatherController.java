package weather.controllers;

import net.aksingh.owmjapis.model.CurrentWeather;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import weather.services.WeatherService;

@Controller("/")
public class WeatherController {
    private static final Logger LOG = Logger.getLogger(WeatherController.class);
    private static final int LATITUDE_MIN = -90;
    private static final int LATITUDE_MAX = 90;
    private static final int LONGITUDE_MIN = -180;
    private static final int LONGITUDE_MAX = 180;

    @Autowired
    WeatherService weatherService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(
            @RequestParam(value = "userName", required = false, defaultValue = "anonymous") String userName,
            @RequestParam(value = "cityId", required = false, defaultValue = "") String cityId,
            @RequestParam(value = "cityName", required = false, defaultValue = "") String cityName,
            @RequestParam(value = "latitude", required = false, defaultValue = "") String latitude,
            @RequestParam(value = "longitude", required = false, defaultValue = "") String longitude,
            Model model) {

        CurrentWeather currentWeather;
        String weather;

        LOG.info("USER: " + userName + " SEARCH WITH: { id=" + cityId + ", cityName=" + cityName + ", lon=" + longitude + ", lat=" + latitude + "}");

        if (!StringUtils.isEmpty(userName)) {
            String errorString = "NOT AUTHORIZED";
            LOG.warn(errorString);
            model.addAttribute("errorString", errorString);
            return "exceptionPage";
        } else if (userName.equals("anonymous")) {
            String errorString = "anonymous users are not allowed";
            LOG.warn(errorString);
            model.addAttribute("errorString", errorString);
            return "exceptionPage";
        }

        // ALL PARAMS CHECK
        if ((StringUtils.isEmpty(cityId)) && (StringUtils.isEmpty(cityName)) && (StringUtils.isEmpty(latitude) && (StringUtils.isEmpty(longitude)))) {
            String errorString = "All params are empty";
            LOG.warn(errorString);
            model.addAttribute("errorString", errorString);
            return "exceptionPage";
        }

        // CITY ID CHECK
        try {
            if (!StringUtils.isEmpty(cityId) && Integer.parseInt(cityId) > 0) {
                currentWeather = weatherService.getWeather(cityId, null, 0, 0).get();
                weather = currentWeather == null ? "" : "WEATHER FOR CITY ID (" + cityId + "): \n" + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";

                LOG.debug("Search for city id: " + cityId);
                if (weather == null || weather.isEmpty()) {
                    String errorString = "Weather for cityId = " + cityId + " not found";
                    model.addAttribute("errorString", errorString);
                    LOG.warn(errorString);
                    return "exceptionPage";
                }
                model.addAttribute("cityId", cityId);
                model.addAttribute("weather", weather);
                LOG.info("USER: " + userName + " GET WEATHER: " + weather);
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
            if (!StringUtils.isEmpty(cityName)) {
                currentWeather = weatherService.getWeather(null, cityName, 0, 0).get();
                weather = currentWeather == null ? "" : "WEATHER FOR CITY NAME (" + cityName + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
                LOG.debug("Search for city name: " + cityName);
                if (StringUtils.isEmpty(weather)) {
                    String errorString = "Weather for cityName = " + cityName + " not found";
                    model.addAttribute("errorString", errorString);
                    LOG.warn(errorString);
                    return "exceptionPage";
                }
                model.addAttribute("cityName", cityName);
                model.addAttribute("weather", weather);
                LOG.info("USER: " + userName + " GET WEATHER: " + weather);
                return "search";
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            model.addAttribute("errorString", e.getMessage());
            return "exceptionPage";
        }

        // COORDS CHECK
        try {
            if (!StringUtils.isEmpty(latitude) && !StringUtils.isEmpty(longitude)) {
                if (Double.parseDouble(latitude) < LATITUDE_MIN || Double.parseDouble(latitude) > LATITUDE_MAX || Double.parseDouble(longitude) < LONGITUDE_MIN || Double.parseDouble(longitude) > LONGITUDE_MAX) {
                    String errorString = "Bad coords: lon=" + longitude + "(min -180, max 180) & lat=" + latitude + "(min -90, max 90)";
                    model.addAttribute("errorString", errorString);
                    LOG.warn(errorString);
                    return "exceptionPage";
                }
                currentWeather = weatherService.getWeather(null, null, Double.parseDouble(latitude), Double.parseDouble(longitude)).get();
                weather = currentWeather == null ? "" : "WEATHER FOR COORDINATES (lat: " + latitude + ", lon: " + longitude + "): " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
                LOG.debug("Search for coords: lat=" + latitude + ", lon=" + longitude);
                if (weather == null || weather.isEmpty()) {
                    String errorString = "Weather for coords: lon=" + longitude + " & lat=" + latitude + " not found";
                    model.addAttribute("errorString", errorString);
                    LOG.warn(errorString);
                    return "exceptionPage";
                }
                model.addAttribute("latitude", latitude);
                model.addAttribute("longitude", longitude);
                model.addAttribute("weather", weather);
                LOG.info("USER: " + userName + " GET WEATHER: " + weather);
                return "search";
            } else if ((StringUtils.isEmpty(latitude) && !StringUtils.isEmpty(longitude)) || (StringUtils.isEmpty(longitude) && !StringUtils.isEmpty(latitude))) {
                throw new NumberFormatException();
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
            currentWeather = weatherService.getWeather("498817", null, 0, 0).get();
            weather = currentWeather == null ? "" : "NOT FOUND. Default: " + currentWeather.getMainData().getTemp() + "°C " + currentWeather.getWeatherList().get(0).getDescription() + " at " + currentWeather.getCityName() + " now";
            LOG.debug("DEFAULT search");
            if (StringUtils.isEmpty(weather)) {
                String errorString = "Default weather not found";
                model.addAttribute("errorString", errorString);
                LOG.error(errorString);
                return "exceptionPage";
            }
            model.addAttribute("cityId", cityId);
            model.addAttribute("cityName", cityName);
            model.addAttribute("latitude", latitude);
            model.addAttribute("longitude", longitude);
            model.addAttribute("weather", weather);
            LOG.info("USER: " + userName + " GET WEATHER: " + weather);
            return "search";
        } catch (Exception e) {
            LOG.error(e.getMessage());
            model.addAttribute("errorString", e.getMessage());
            return "exceptionPage";
        }
    }
}