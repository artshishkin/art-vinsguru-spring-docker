package net.shyshkin.study.docker.candidateservice;

import net.shyshkin.study.docker.candidateservice.dto.CandidateDetailsDto;
import net.shyshkin.study.docker.candidateservice.dto.CandidateDto;
import net.shyshkin.study.docker.candidateservice.entity.Candidate;
import net.shyshkin.study.docker.candidateservice.repository.CandidateRepository;
import net.shyshkin.study.docker.compose.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("mockserver")
@TestPropertySource(properties = {
        "app.job-service.base-url: http://localhost:${MOCK_SERVER_PORT}"
})
class CandidateServiceApplicationIT extends BaseTest {

    @Autowired
    WebTestClient webTestClient;

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
    void getCandidateById_1() {
        //given
        String id = "1";

        //when
        webTestClient
                .get().uri("/candidates/{id}", id)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBody(CandidateDetailsDto.class)
                .value(dto -> assertAll(
                                () -> assertThat(dto).hasNoNullFieldsOrProperties(),
                                () -> assertThat(dto.getId()).isEqualTo("1"),
                                () -> assertThat(dto.getName()).isEqualTo("Artem Shyshkin"),
                                () -> assertThat(dto.getSkills()).containsExactlyInAnyOrder("java", "git", "spring", "mongo", "postgres", "docker"),
                                () -> assertThat(dto.getRecommendedJobs()).isNotEmpty()
                        )
                );
    }

    @ParameterizedTest
    @Order(25)
    @MethodSource
    void getCandidateById_23(String id, String expectedName, String[] expectedSkills) {

        //when
        webTestClient
                .get().uri("/candidates/{id}", id)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBody(CandidateDetailsDto.class)
                .value(dto -> assertAll(
                                () -> assertThat(dto).hasNoNullFieldsOrProperties(),
                                () -> assertThat(dto.getId()).isEqualTo(id),
                                () -> assertThat(dto.getName()).isEqualTo(expectedName),
                                () -> assertThat(dto.getSkills()).containsExactlyInAnyOrder(expectedSkills),
                                () -> assertThat(dto.getRecommendedJobs()).isEmpty()
                        )
                );
    }

    static Stream<Arguments> getCandidateById_23() {
        return Stream.of(
                Arguments.of("2", "Kateryna Shyshkin", new String[]{"jira", "qa", "git"}),
                Arguments.of("3", "Arina Shyshkina", new String[]{"english", "scratch", "climbing", "graphity"})
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