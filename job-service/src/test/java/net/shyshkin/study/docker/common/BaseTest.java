package net.shyshkin.study.docker.common;

import net.shyshkin.study.docker.util.VersionUtil;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public abstract class BaseTest {

    private static final int MONGO_PORT = 27017;
    private static final String INIT_JS = "/docker-entrypoint-initdb.d/init-db.js";
    private static final String INIT_JS_HOST_LOCATION = "./../docker-compose/art-vinsguru-docker/data-mongo/init-db.js";

    @Container
    protected static GenericContainer<?> mongo = new GenericContainer<>(
            DockerImageName
                    .parse("mongo")
                    .withTag(VersionUtil.getVersion("MONGO_VERSION")))
            .withExposedPorts(MONGO_PORT)
            .withCopyFileToContainer(MountableFile.forHostPath(INIT_JS_HOST_LOCATION), INIT_JS)
            .waitingFor(Wait.forListeningPort());

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", () -> mongo.getHost());
        registry.add("spring.data.mongodb.port", () -> mongo.getMappedPort(MONGO_PORT));
    }
}
