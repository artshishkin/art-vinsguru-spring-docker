package net.shyshkin.study.docker.jobservice;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.docker.common.BaseTest;
import net.shyshkin.study.docker.jobservice.dto.JobDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@AutoConfigureWebTestClient
class JobServiceApplicationIT extends BaseTest {

    @Autowired
    WebTestClient client;

    @Test
    void allJobsTest() {

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
                .value(jobs -> assertThat(jobs)
                        .allSatisfy(job -> assertThat(job).hasNoNullFieldsOrProperties()))
        ;
    }


}