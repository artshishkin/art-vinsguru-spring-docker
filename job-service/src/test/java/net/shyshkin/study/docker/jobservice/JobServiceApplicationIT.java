package net.shyshkin.study.docker.jobservice;

import net.shyshkin.study.docker.jobservice.dto.JobDto;
import net.shyshkin.study.docker.util.VersionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
@ContextConfiguration(initializers = JobServiceApplicationIT.Initializer.class)
class JobServiceApplicationIT {

    private static final int MONGO_PORT = 27017;
    private static final String INIT_JS = "/docker-entrypoint-initdb.d/init-db.js";
    private static final String INIT_JS_HOST_LOCATION = "./../docker-compose/art-vinsguru-docker/data-mongo/init-db.js";

    @Autowired
    WebTestClient client;

    @Container
    static GenericContainer<?> mongo = new GenericContainer<>(
            DockerImageName
                    .parse("mongo")
                    .withTag(VersionUtil.getVersion("MONGO_VERSION")))
            .withExposedPorts(MONGO_PORT)
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "password")
            .withCopyFileToContainer(MountableFile.forHostPath(INIT_JS_HOST_LOCATION), INIT_JS)
            .waitingFor(Wait.forListeningPort());

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
                .value(jobs -> assertThat(jobs)
                        .allSatisfy(job -> assertThat(job).hasNoNullFieldsOrProperties()))
        ;
    }

    protected static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Integer mongoPort = mongo.getMappedPort(MONGO_PORT);
            TestPropertyValues
                    .of("spring.data.mongodb.port=" + mongoPort)
                    .applyTo(applicationContext.getEnvironment());
        }
    }

}