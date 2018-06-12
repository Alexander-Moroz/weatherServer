package weather.config;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
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