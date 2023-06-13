package it.unisa.c07.biblionet.config;

import it.unisa.c07.biblionet.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean jwtFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new JwtFilter());

        filter.addUrlPatterns("/biblioteca/*");

        return filter;
    }
}