package com.maemresen.tcw.with.annotation;

import com.github.dockerjava.api.command.CreateContainerCmd;
import java.util.function.Consumer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
@Testcontainers
abstract class AbstractBaseRedisServiceTest {

    @Container
    final GenericContainer testRedisContainer;

    AbstractBaseRedisServiceTest() {
        DockerImageName dockerImageName = DockerImageName.parse("redis:7.0.5-alpine");
        this.testRedisContainer = new GenericContainer(dockerImageName)
            .withCreateContainerCmdModifier((Consumer<CreateContainerCmd>) cmd -> cmd.withName("TEST-REDIS"))
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());
    }

    String getTestRedisContainerHost() {
        return this.testRedisContainer.getHost();
    }

    Integer getTestRedisContainerPort() {
        return this.testRedisContainer.getMappedPort(6379);
    }
}
