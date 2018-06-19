package weather.controllers;

import net.aksingh.owmjapis.model.CurrentWeather;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import weather.exceptions.WeatherException;
import weather.services.WeatherService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller("/")
public class WeatherController {
    private static final Logger LOG = Logger.getLogger(WeatherController.class);

    @Autowired
    WeatherService weatherService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search (
            @RequestParam(value = "userName", required = false, defaultValue = "anonymous") String userName,
            @RequestParam(value = "cityId", required = false, defaultValue = "") String cityId,
            @RequestParam(value = "cityName", required = false, defaultValue = "") String cityName,
            @RequestParam(value = "latitude", required = false, defaultValue = "") String latitude,
            @RequestParam(value = "longitude", required = false, defaultValue = "") String longitude,
            Model model) throws ExecutionException, InterruptedException, WeatherException {

        CurrentWeather currentWeather = null;
        String weather = "";

        LOG.info("USER: " + userName + " SEARCH WITH: { id=" + cityId + ", cityName=" + cityName + ", lon=" + longitude + ", lat=" + latitude + "}");

        if (StringUtils.isEmpty(userName)) {
            String errorString = "NOT AUTHORIZED";
            throw new WeatherException("<" + userName + "> " + errorString);
        } else if (userName.equals("anonymous")) {
            String errorString = "anonymous users are not allowed";
            throw new WeatherException("<" + userName + "> " + errorString);
        }

        currentWeather = weatherService.getWeather(userName, cityId, cityName, latitude, longitude).get();
        weather = "Weather for city " + currentWeather.getCityName() + " is " + currentWeather.getCloudData().toString() + " and temp: " + currentWeather.getMainData().getTemp() + "°C";

        model.addAttribute("cityId", cityId);
        model.addAttribute("cityName", cityName);
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("weather", weather);
        LOG.info("USER: " + userName + " GET WEATHER: " + weather);
        return "search";
    }

    @ExceptionHandler(WeatherException.class)
    public ModelAndView exception(WeatherException e) {
        Map<String, Object> params = new HashMap<>();
        params.put("exception", e.getMessage());
        LOG.error(e.getMessage());
        return new ModelAndView("exceptionPage", params);
    }

    @ExceptionHandler(InterruptedException.class)
    public ModelAndView exception(InterruptedException e) {
        Map<String, Object> params = new HashMap<>();
        params.put("exception", e.getMessage());
        LOG.error(e.getMessage());
        return new ModelAndView("exceptionPage", params);
    }

    @ExceptionHandler(ExecutionException.class)
    public ModelAndView exception(ExecutionException e) {
        Map<String, Object> params = new HashMap<>();
        params.put("exception", e.getMessage());
        LOG.error(e.getMessage());
        return new ModelAndView("exceptionPage", params);
    }
}