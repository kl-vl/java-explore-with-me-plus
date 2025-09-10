package ru.practicum.stats;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientRestStatConfig {

    @Value("${stats-server.url:http://localhost:9090}")
    private String statsServerUrl;

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient statsRestClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder
                .baseUrl(statsServerUrl)
                .build();
    }

    @Bean
    public ClientRestStat clientRestStat(RestClient statsRestClient) {
        return new ClientRestStatImpl(statsRestClient);
    }
}