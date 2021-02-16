package com.amoroz.weather.config;

import com.amoroz.weather.config.props.WeatherProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final WeatherProperties weatherProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (weatherProperties.isSecurityEnabled()) {
            http
                    .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin().loginPage("/login")
                    .defaultSuccessUrl("/searchForm").permitAll()
                    .and()
                    .logout().permitAll();
            log.debug("SECURITY ENABLED");
        } else {
            http.csrf().disable();
            log.debug("SECURITY DISABLED");
        }
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        var users = IntStream.range(1, 5)
                .mapToObj(i -> User.withDefaultPasswordEncoder()
                        .username("user" + i)
                        .password("p")
                        .roles("USER")
                        .build())
                .collect(Collectors.toSet());
        return new InMemoryUserDetailsManager(users);
    }
}
