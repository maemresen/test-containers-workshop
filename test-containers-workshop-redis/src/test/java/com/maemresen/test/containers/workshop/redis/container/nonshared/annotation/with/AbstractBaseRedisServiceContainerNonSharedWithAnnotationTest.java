package com.maemresen.test.containers.workshop.redis.container.nonshared.annotation.with;

import com.github.dockerjava.api.command.CreateContainerCmd;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
@Testcontainers
abstract class AbstractBaseRedisServiceContainerNonSharedWithAnnotationTest {

    @Container
    final GenericContainer testRedisContainer;

    AbstractBaseRedisServiceContainerNonSharedWithAnnotationTest() {
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
