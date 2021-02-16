package com.amoroz.weather.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ws")
@Data
public class WeatherProperties {

    private String appid;
    private boolean securityEnabled = true;
    private String threadNamePrefix = "AsyncTaskExecutorThread-";
    private int corePoolSize = 10;
    private int maxPoolSize = 10;
    private int queueCapacity = 1000;
    private int keepAliveSeconds = 300;
}
