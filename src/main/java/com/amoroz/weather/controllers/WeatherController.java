package com.amoroz.weather.controllers;

import com.amoroz.weather.exceptions.WeatherException;
import com.amoroz.weather.model.SearchRequest;
import com.amoroz.weather.services.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
@Validated
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping(path = {"/", "/searchForm"})
    public String searchForm(HttpServletRequest httpServletRequest, Model model) {
        model.addAttribute("httpServletRequest", httpServletRequest);
        return "searchForm";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/exceptionPage")
    public String exceptionPage() {
        return "exceptionPage";
    }

    @SneakyThrows
    @PostMapping(value = "/searchResult")
    public String searchResult(@Valid SearchRequest searchRequest,
                               Model model) {
        var currentWeather = weatherService.getWeather(searchRequest).get();
        model.addAttribute("searchRequest", searchRequest);
        model.addAttribute("weather", currentWeather);
        return "searchResult";
    }

    @ExceptionHandler({WeatherException.class, BindException.class, InterruptedException.class, ExecutionException.class})
    public ModelAndView exception(Exception e) {
        var params = new HashMap<String, Object>();
        params.put("exception", e.getMessage());
        log.error(e.getMessage());
        return new ModelAndView("exceptionPage", params);
    }
}