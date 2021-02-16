package com.amoroz.weather.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.aksingh.owmjapis.core.OWM;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import com.amoroz.weather.config.props.WeatherProperties;

@Configuration
@EnableAsync
@Slf4j
@RequiredArgsConstructor
public class MainConfig {

    private final WeatherProperties weatherProperties;

    @Bean(name = "weatherSiteApiExecutor")
    public TaskExecutor threadPoolTaskExecutor() {

        var bean = new ThreadPoolTaskExecutor();
        bean.setThreadNamePrefix(weatherProperties.getThreadNamePrefix());
        bean.setCorePoolSize(weatherProperties.getCorePoolSize());
        bean.setMaxPoolSize(weatherProperties.getMaxPoolSize());
        bean.setQueueCapacity(weatherProperties.getQueueCapacity());
        bean.setKeepAliveSeconds(weatherProperties.getKeepAliveSeconds());
        bean.afterPropertiesSet();

        if (log.isDebugEnabled()) {
            log.debug("ThreadPoolTaskExecutor initialized with params: {}", bean);
        }

        return bean;
    }

    @Bean
    public OWM owm() {
        var bean = new OWM(weatherProperties.getAppid());
        bean.setUnit(OWM.Unit.METRIC);
        return bean;
    }
}
