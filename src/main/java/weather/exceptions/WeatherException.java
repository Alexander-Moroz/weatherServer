package weather.exceptions;

public class WeatherException extends RuntimeException {
    public WeatherException(String message) {
        super(message);
    }
}