package com.stefandanila.api.filter;

import com.stefandanila.api.service.limiter.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterFilterRegistrationBean {

    private final RateLimiterService rateLimiterService;

    @Autowired
    public RateLimiterFilterRegistrationBean(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Bean
    public FilterRegistrationBean<RateLimiterFilter> rateLimiterFilter() {
        FilterRegistrationBean<RateLimiterFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimiterFilter(rateLimiterService));
        registrationBean.addUrlPatterns("/foo", "/bar");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
