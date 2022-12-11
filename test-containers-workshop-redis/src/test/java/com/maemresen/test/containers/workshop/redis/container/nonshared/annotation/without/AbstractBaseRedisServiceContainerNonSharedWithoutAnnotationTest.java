package com.maemresen.test.containers.workshop.redis.container.nonshared.annotation.without;

import com.github.dockerjava.api.command.CreateContainerCmd;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
abstract class AbstractBaseRedisServiceContainerNonSharedWithoutAnnotationTest {

    final GenericContainer testRedisContainer;

    AbstractBaseRedisServiceContainerNonSharedWithoutAnnotationTest() {
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

    @BeforeEach
    void setUp(TestInfo testInfo) {
        testRedisContainer.start();
        System.out.printf("Test:%s::Starting::%s%n", testInfo.getDisplayName(), testRedisContainer.getContainerName());
        System.out.printf("Test:%s::Connecting to Redis%n", testInfo.getDisplayName());
    }

    @AfterEach
    void wrapUp(TestInfo testInfo) {
        if (testRedisContainer.isRunning()) {
            System.out.printf("Test:%s::Stopping::%s%n", testInfo.getDisplayName(), testRedisContainer.getContainerName());
            testRedisContainer.stop();
        }
    }
}
