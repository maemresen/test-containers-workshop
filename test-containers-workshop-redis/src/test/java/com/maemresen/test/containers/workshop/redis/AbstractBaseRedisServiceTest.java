package com.maemresen.test.containers.workshop.redis;

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
abstract class AbstractBaseRedisServiceTest {

    final GenericContainer testRedisContainer;

    AbstractBaseRedisServiceTest() {
        DockerImageName dockerImageName = DockerImageName.parse("redis:7.0.5-alpine");
        this.testRedisContainer = new GenericContainer(dockerImageName)
            .withCreateContainerCmdModifier((Consumer<CreateContainerCmd>) cmd -> cmd.withName("TEST-REDIS"))
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        testRedisContainer.start();
        System.out.printf("Test:%s::Starting::%s:%s%n"
            , testInfo.getDisplayName()
            , testRedisContainer.getContainerId()
            , testRedisContainer.getContainerName());
    }

    @AfterEach
    void wrapUp(TestInfo testInfo) {
        if (testRedisContainer.isRunning()) {
            System.out.printf("Test:%s::Stopping::%s:%s%n"
                , testInfo.getDisplayName()
                , testRedisContainer.getContainerId()
                , testRedisContainer.getContainerName());
            testRedisContainer.stop();
        }
    }

    protected String getTestRedisContainerHost() {
        return testRedisContainer.getHost();
    }

    protected Integer getTestRedisContainerPort() {
        return testRedisContainer.getMappedPort(6379);
    }

    protected RedisService getRedisService(String host, Integer port) {
        String connectionString = String.format("redis://%s:%d/%d", host, port, 0);
        return new RedisService(connectionString);
    }
}
