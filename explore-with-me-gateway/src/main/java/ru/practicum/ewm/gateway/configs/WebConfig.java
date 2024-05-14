package ru.practicum.ewm.gateway.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;

@Configuration
public class WebConfig {

    @Bean
    public RestTemplate basicServiceRestTemplate(@Value("${basic-service.url}") String serverUrl, RestTemplateBuilder builder) {
        return builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .setConnectTimeout(Duration.ofSeconds(5))
                .build();
    }
}
