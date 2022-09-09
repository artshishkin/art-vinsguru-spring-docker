package net.shyshkin.study.docker.candidateservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.docker.candidateservice.dto.JobDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobClient {

    private final WebClient jobWebClient;

    public Mono<Set<JobDto>> getRecommendedJobs(Set<String> skills) {
        return jobWebClient.get()
                .uri(u -> u.path("/jobs")
                        .queryParam("skills", skills)
                        .build()
                )
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(JobDto.class)
                .collect(Collectors.toSet())
                .doOnError(t -> log.debug("Error while getting jobs with skills", t))
                .onErrorReturn(Set.of());
    }

}
