package com.example.DealerFlow.Client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class PythonApiConfig {

    @Bean
    public RestClient pythonApiRestClient(PythonApiProperties properties) {
        if (properties.getBaseUrl() == null) {
            throw new IllegalStateException("python-api.base-url must be configured");
        }
        if (properties.getConnectTimeout() == null || properties.getReadTimeout() == null) {
            throw new IllegalStateException("python-api timeouts must be configured");
        }

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.getConnectTimeout());
        requestFactory.setReadTimeout(properties.getReadTimeout());

        return RestClient.builder()
                .baseUrl(properties.getBaseUrl().toString())
                .requestFactory(requestFactory)
                .build();
    }
}
