package net.shyshkin.study.docker.compose;

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

    private static final int MONGO_PORT = 27017;
    private static final String MONGO = "mongo";

    private static final String[] COMPOSE_FILES_PATHS = {
            "../docker-compose/art-vinsguru-docker/common.yml",
            "../docker-compose/art-vinsguru-docker/mongo_stack.yaml"
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
                .withEnv("MONGO_HOST_PORT", "0")
                .withExposedService(MONGO, MONGO_PORT, Wait.forListeningPort())
                .start();
        registry.add("spring.data.mongodb.host", () -> composeContainer.getServiceHost(MONGO, MONGO_PORT));
        registry.add("spring.data.mongodb.port", () -> composeContainer.getServicePort(MONGO, MONGO_PORT));
    }
}
