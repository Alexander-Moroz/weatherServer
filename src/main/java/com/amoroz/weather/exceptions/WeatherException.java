package com.amoroz.weather.exceptions;

public class WeatherException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(Exception e) {
        super(e);
    }
}