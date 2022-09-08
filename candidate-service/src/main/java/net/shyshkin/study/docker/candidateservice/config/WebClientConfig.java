package net.shyshkin.study.docker.candidateservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient jobWebClient(@Value("${app.job-service.base-url}") String jobServiceBaseUrl) {
        return WebClient.builder()
                .baseUrl(jobServiceBaseUrl)
                .build();
    }

}
