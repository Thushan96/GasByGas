package com.esoft.gascollect.config;

import com.esoft.gascollect.security.JwtRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public FilterConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtRequestFilter);
//        registrationBean.addUrlPatterns("/users/*"); // Apply the filter to specific URL patterns
        return registrationBean;
    }
}
