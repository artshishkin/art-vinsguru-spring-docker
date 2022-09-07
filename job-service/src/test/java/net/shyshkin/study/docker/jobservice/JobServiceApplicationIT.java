package net.shyshkin.study.docker.jobservice;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.docker.compose.BaseTest;
import net.shyshkin.study.docker.jobservice.dto.JobDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JobServiceApplicationIT extends BaseTest {

    @Autowired
    WebTestClient client;

    private static JobDto exampleJob;

    @Test
    @Order(10)
    void allJobsTest_applicationJson() {

        //when
        this.client
                .get().uri("/jobs")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBodyList(JobDto.class)
                .hasSize(5)
                .value(jobs -> log.debug("Result: {}", jobs))
                .value(jobs -> exampleJob = jobs.get(0))
                .value(jobs -> assertThat(jobs)
                        .allSatisfy(job -> assertThat(job).hasNoNullFieldsOrProperties()))
        ;
    }

    @Test
    @Order(20)
    void allJobsTest_textEventStream() {

        //when
        this.client
                .get().uri("/jobs")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBodyList(JobDto.class)
                .hasSize(5)
                .value(jobs -> log.debug("Result: {}", jobs))
                .value(jobs -> assertThat(jobs)
                        .allSatisfy(job -> assertThat(job).hasNoNullFieldsOrProperties()))
        ;
    }

    @Test
    @Order(30)
    void getJobsBySkillsTest() {

        //when
        this.client
                .get().uri("/jobs?skills=java")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBodyList(JobDto.class)
                .hasSize(3)
                .value(jobs -> log.debug("Result: {}", jobs))
                .value(jobs -> assertThat(jobs)
                        .allSatisfy(job -> assertThat(job).hasNoNullFieldsOrProperties()))
        ;
    }

    @Test
    @Order(40)
    void getJobByIdTest() {

        //given
        String id = exampleJob.getId();

        //when
        this.client
                .get().uri("/jobs/{id}", id)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBody(JobDto.class)
                .value(job -> assertThat(job).isEqualTo(exampleJob));
    }

    @Test
    @Order(50)
    void createNewJobTest() {

        //given
        String newJobJson = "{\n" +
                "  \"description\": \"Senior .Net Developer\",\n" +
                "  \"company\": \"Amazon\",\n" +
                "  \"skills\": [\n" +
                "    \"c\",\n" +
                "    \"mongo\",\n" +
                "    \"postgres\",\n" +
                "    \"docker\"\n" +
                "  ],\n" +
                "  \"salary\": 90000,\n" +
                "  \"remote\": true\n" +
                "}";

        //when
        this.client
                .post().uri("/jobs")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(newJobJson)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBody(JobDto.class)
                .value(job -> assertAll(
                                () -> assertThat(job).hasNoNullFieldsOrProperties(),
                                () -> assertThat(job.getDescription()).isEqualTo("Senior .Net Developer"),
                                () -> assertThat(job.getCompany()).isEqualTo("Amazon"),
                                () -> assertThat(job.getSalary()).isEqualTo(90000),
                                () -> assertThat(job.getRemote()).isTrue(),
                                () -> assertThat(job.getSkills())
                                        .hasSize(4)
                                        .containsExactlyInAnyOrder("c", "mongo", "postgres", "docker")
                        )
                )
                .value(job -> exampleJob = job)
        ;
    }

    @Test
    @Order(60)
    void updateJobTest() {

        //given
        JobDto updated = new JobDto();
        BeanUtils.copyProperties(exampleJob, updated);
        String id = updated.getId();
        updated.setCompany("Art's company");

        //when
        this.client
                .put().uri("/jobs/{id}", id)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(updated)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBody(JobDto.class)
                .value(job -> assertAll(
                                () -> assertThat(job).hasNoNullFieldsOrProperties(),
                                () -> assertThat(job.getDescription()).isEqualTo("Senior .Net Developer"),
                                () -> assertThat(job.getCompany()).isEqualTo("Art's company"),
                                () -> assertThat(job.getSalary()).isEqualTo(90000),
                                () -> assertThat(job.getRemote()).isTrue(),
                                () -> assertThat(job.getSkills())
                                        .hasSize(4)
                                        .containsExactlyInAnyOrder("c", "mongo", "postgres", "docker")
                        )
                )
                .value(job -> exampleJob = job)
        ;
    }

    @Test
    @Order(70)
    void deleteJobTest() {

        //given
        String id = exampleJob.getId();

        //when
        this.client
                .delete().uri("/jobs/{id}", id)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectBody()
                .isEmpty();
    }

}