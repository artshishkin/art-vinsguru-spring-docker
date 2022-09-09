package net.shyshkin.study.docker.compose.dto;

import lombok.Data;

@Data(staticConstructor = "create")
public class Service {

    private final String name;
    private final int port;
    private final String hostPort;
    private final String hostPortEnvVariable;

}
