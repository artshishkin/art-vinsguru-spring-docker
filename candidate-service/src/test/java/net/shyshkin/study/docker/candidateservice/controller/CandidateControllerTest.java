package net.shyshkin.study.docker.candidateservice.controller;

import net.shyshkin.study.docker.candidateservice.client.JobClient;
import net.shyshkin.study.docker.candidateservice.dto.CandidateDto;
import net.shyshkin.study.docker.candidateservice.entity.Candidate;
import net.shyshkin.study.docker.candidateservice.repository.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest
@AutoConfigureWebTestClient
class CandidateControllerTest {

    @MockBean
    CandidateRepository repository;

    @MockBean
    JobClient jobClient;

    @Autowired
    WebTestClient webTestClient;

    private final Candidate[] mockCandidates = {
            Candidate.builder()
                    .id("1")
                    .name("Art")
                    .skills(Set.of("java", "docker", "spring", "mongo", "postgresql"))
                    .build(),
            Candidate.builder()
                    .id("2")
                    .name("Kate")
                    .skills(Set.of("jira", "qa", "git", "postman"))
                    .build()
    };

    @Test
    void getAllCandidates_json() {

        //given
        given(repository.findAll()).willReturn(Flux.just(mockCandidates));

        //when
        webTestClient
                .get().uri("/candidates")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBodyList(CandidateDto.class)
                .hasSize(2)
                .value(candidates -> assertThat(candidates)
                        .allSatisfy(dto -> assertAll(
                                () -> assertThat(dto).hasNoNullFieldsOrProperties(),
                                () -> assertThat(dto.getId()).isIn("1", "2"),
                                () -> assertThat(dto.getName()).isIn("Art", "Kate")
                        ))
                        .anySatisfy(dto -> assertThat(dto.getSkills()).containsExactlyInAnyOrder(mockCandidates[0].getSkills().toArray(new String[0])))
                        .anySatisfy(dto -> assertThat(dto.getSkills()).containsExactlyInAnyOrder(mockCandidates[0].getSkills().toArray(new String[1])))
                );
        then(repository).should().findAll();
    }

    @Test
    void getAllCandidates_eventStream() {

        //given
        given(repository.findAll()).willReturn(Flux.just(mockCandidates));

        //when
        webTestClient
                .get().uri("/candidates")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBodyList(CandidateDto.class)
                .hasSize(2)
                .value(candidates -> assertThat(candidates)
                        .allSatisfy(dto -> assertAll(
                                () -> assertThat(dto).hasNoNullFieldsOrProperties(),
                                () -> assertThat(dto.getId()).isIn("1", "2"),
                                () -> assertThat(dto.getName()).isIn("Art", "Kate")
                        ))
                        .anySatisfy(dto -> assertThat(dto.getSkills()).containsExactlyInAnyOrder(mockCandidates[0].getSkills().toArray(new String[0])))
                        .anySatisfy(dto -> assertThat(dto.getSkills()).containsExactlyInAnyOrder(mockCandidates[0].getSkills().toArray(new String[1])))
                );
        then(repository).should().findAll();
    }

    @Test
    void getCandidateById() {
        //given
        String id = "1";
        given(repository.findById(anyString())).willReturn(Mono.just(mockCandidates[0]));
        given(jobClient.getRecommendedJobs(any())).willReturn(Mono.just(Set.of()));

        //when
        webTestClient
                .get().uri("/candidates/{id}", id)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBody(CandidateDto.class)
                .value(dto -> assertAll(
                        () -> assertThat(dto).hasNoNullFieldsOrProperties(),
                        () -> assertThat(dto.getId()).isEqualTo("1"),
                        () -> assertThat(dto.getName()).isEqualTo("Art"),
                        () -> assertThat(dto.getSkills()).containsExactlyInAnyOrder(mockCandidates[0].getSkills().toArray(new String[0])))
                );
        then(repository).should().findById(eq(id));

    }

    @Test
    void createCandidate() {
        //given
        given(repository.save(any(Candidate.class))).willReturn(Mono.just(mockCandidates[0]));

        //when
        webTestClient
                .post().uri("/candidates")
                .bodyValue(mockCandidates[0])
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBody(CandidateDto.class)
                .value(dto -> assertAll(
                        () -> assertThat(dto).hasNoNullFieldsOrProperties(),
                        () -> assertThat(dto.getId()).isEqualTo("1"),
                        () -> assertThat(dto.getName()).isEqualTo("Art"),
                        () -> assertThat(dto.getSkills()).containsExactlyInAnyOrder(mockCandidates[0].getSkills().toArray(new String[0])))
                );
        then(repository).should().save(any(Candidate.class));
    }

}