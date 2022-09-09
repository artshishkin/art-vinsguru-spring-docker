package net.shyshkin.study.docker.compose;

import net.shyshkin.study.docker.compose.dto.Service;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

@Testcontainers
public abstract class BaseTest {

    private static final Service MONGO = Service
            .create("mongo", 27017, "0", "MONGO_HOST_PORT");
    private static final Service MOCKSERVER = Service
            .create("mockserver", 1080, "0", "MOCKSERVER_HOST_PORT");

    private static final String[] COMPOSE_FILES_PATHS = {
            "../docker-compose/art-vinsguru-docker/common.yml",
            "../docker-compose/art-vinsguru-docker/mongo_stack.yaml",
            "../docker-compose/art-vinsguru-docker/mock-server.yml"
    };

//    private static final String ENV_FILE_PATH = "../docker-compose/art-vinsguru-docker/.env";
    private static final String ENV_FILE_PATH = ".env";

    protected static DockerComposeContainer<?> composeContainer = new DockerComposeContainer<>(
            Arrays.stream(COMPOSE_FILES_PATHS)
                    .map(path -> Paths.get(path).toAbsolutePath().normalize().toFile())
                    .collect(Collectors.toList())
    )
            .withOptions("--env-file " + ENV_FILE_PATH);

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        composeContainer
                .withEnv(MONGO.getHostPortEnvVariable(), MONGO.getHostPort())
                .withEnv(MOCKSERVER.getHostPortEnvVariable(), MOCKSERVER.getHostPort())
                .withEnv("COMPOSE_PROFILES", "mockserver")
                .withExposedService(MONGO.getName(), MONGO.getPort(), Wait.forListeningPort())
                .withExposedService(MOCKSERVER.getName(), MOCKSERVER.getPort(), Wait.forListeningPort())
                .start();
        registry.add("spring.data.mongodb.host", () -> composeContainer.getServiceHost(MONGO.getName(), MONGO.getPort()));
        registry.add("spring.data.mongodb.port", () -> composeContainer.getServicePort(MONGO.getName(), MONGO.getPort()));
        registry.add("MOCK_SERVER_PORT", () -> composeContainer.getServicePort(MOCKSERVER.getName(), MOCKSERVER.getPort()));
    }
}
