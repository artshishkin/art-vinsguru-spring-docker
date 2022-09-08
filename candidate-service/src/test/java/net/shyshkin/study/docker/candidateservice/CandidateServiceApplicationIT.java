package net.shyshkin.study.docker.candidateservice;

import net.shyshkin.study.docker.candidateservice.client.JobClient;
import net.shyshkin.study.docker.candidateservice.dto.CandidateDto;
import net.shyshkin.study.docker.candidateservice.entity.Candidate;
import net.shyshkin.study.docker.candidateservice.repository.CandidateRepository;
import net.shyshkin.study.docker.compose.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CandidateServiceApplicationIT extends BaseTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    JobClient jobClient;

    @Autowired
    CandidateRepository repository;

    @Test
    @Order(10)
    void getAllCandidates_json() {

        //when
        webTestClient
                .get().uri("/candidates")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBodyList(CandidateDto.class)
                .hasSize(4)
                .value(candidates -> assertThat(candidates)
                        .allSatisfy(dto -> assertAll(
                                () -> assertThat(dto).hasNoNullFieldsOrProperties(),
                                () -> assertThat(dto.getSkills()).isNotEmpty()
                        ))
                );
    }

    @Test
    @Order(10)
    void getAllCandidates_eventStream() {

        //when
        webTestClient
                .get().uri("/candidates")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBodyList(CandidateDto.class)
                .hasSize(4)
                .value(candidates -> assertThat(candidates)
                        .allSatisfy(dto -> assertAll(
                                () -> assertThat(dto).hasNoNullFieldsOrProperties(),
                                () -> assertThat(dto.getSkills()).isNotEmpty()
                        ))
                );
    }

    @Test
    @Order(20)
    void getCandidateById() {
        //given
        String id = "1";
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
                        () -> assertThat(dto.getName()).isEqualTo("Artem Shyshkin"),
                        () -> assertThat(dto.getSkills()).containsExactlyInAnyOrder("java", "git", "spring", "mongo", "postgres", "docker"))
                );
    }

    @Test
    @Order(30)
    void createCandidate() {

        //given
        Candidate newCandidate = Candidate.builder()
                .id("345")
                .name("Sergey")
                .skills(Set.of("trade", "cook", "help"))
                .build();
        Long initialCount = repository.count()
                .block();

        //when
        webTestClient
                .post().uri("/candidates")
                .bodyValue(newCandidate)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBody(CandidateDto.class)
                .value(dto -> assertAll(
                        () -> assertThat(dto).hasNoNullFieldsOrProperties(),
                        () -> assertThat(dto.getId()).isEqualTo("345"),
                        () -> assertThat(dto.getName()).isEqualTo("Sergey"),
                        () -> assertThat(dto.getSkills()).containsExactlyInAnyOrder(newCandidate.getSkills().toArray(String[]::new))
                ));
        StepVerifier.create(repository.count())
                .expectNext(initialCount + 1)
                .verifyComplete();
    }

}