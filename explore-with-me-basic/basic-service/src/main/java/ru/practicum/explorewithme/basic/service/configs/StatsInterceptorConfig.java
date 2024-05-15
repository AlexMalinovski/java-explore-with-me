package ru.practicum.explorewithme.basic.service.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.practicum.explorewithme.basic.service.common.controllers.StatsInterceptor;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import java.util.Set;
import java.util.regex.Pattern;

@Configuration
public class StatsInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private StatsClient statsClient;

    @Bean
    StatsInterceptor statsInterceptor() {
        Set<Pattern> uris = Set.of(
                Pattern.compile("^/events$"),
                Pattern.compile("^/events/\\d+$")
        );
        return new StatsInterceptor(uris, statsClient);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statsInterceptor());
    }
}
