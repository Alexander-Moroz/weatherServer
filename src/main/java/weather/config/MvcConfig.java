package weather.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class MvcConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    private static final Logger LOG = Logger.getLogger(MvcConfig.class);

    @Value("${ws.security}")
    private String security;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (!StringUtils.isEmpty(security) && security.equalsIgnoreCase("true")) {
            http
                    .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll();
            LOG.debug("SECURITY ENABLED");
        } else {
            http.csrf().disable();
            LOG.debug("SECURITY DISABLED");
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("searchForm");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/searchForm").setViewName("searchForm");
        registry.addViewController("/search").setViewName("search");
        registry.addViewController("/exceptionPage").setViewName("exceptionPage");
    }

    // Список пользователей в памяти
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        Set<UserDetails> users = new HashSet<>();
        users.add(User.withDefaultPasswordEncoder()
                .username("user1")
                .password("p")
                .roles("USER")
                .build());

        users.add(User.withDefaultPasswordEncoder()
                .username("user2")
                .password("p")
                .roles("USER")
                .build());

        users.add(User.withDefaultPasswordEncoder()
                .username("user3")
                .password("p")
                .roles("USER")
                .build());

        users.add(User.withDefaultPasswordEncoder()
                .username("user4")
                .password("p")
                .roles("USER")
                .build());

        return new InMemoryUserDetailsManager(users);
    }
}