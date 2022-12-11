package com.maemresen.test.containers.workshop.redis;

import com.github.dockerjava.api.command.CreateContainerCmd;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
class RedisService_IOTest {

    final GenericContainer testRedisContainer;
    RedisService redisService;

    RedisService_IOTest() {
        DockerImageName dockerImageName = DockerImageName.parse("redis:7.0.5-alpine");
        this.testRedisContainer = new GenericContainer(dockerImageName)
            .withCreateContainerCmdModifier((Consumer<CreateContainerCmd>) cmd -> cmd.withName("TEST-REDIS"))
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        testRedisContainer.start();
        System.out.printf("Test:%s::Starting::%s%n", testInfo.getDisplayName(), testRedisContainer.getContainerName());
        System.out.printf("Test:%s::Connecting to Redis%n", testInfo.getDisplayName());
        redisService = getRedisService(testRedisContainer.getHost(), testRedisContainer.getMappedPort(6379));
    }

    @AfterEach
    void wrapUp(TestInfo testInfo) {
        if (testRedisContainer.isRunning()) {
            System.out.printf("Test:%s::Stopping::%s%n", testInfo.getDisplayName(), testRedisContainer.getContainerName());
            testRedisContainer.stop();
        }
    }

    protected RedisService getRedisService(String host, Integer port) {
        String connectionString = String.format("redis://%s:%d/%d", host, port, 0);
        return new RedisService(connectionString);
    }

    @Test
    @DisplayName("Put then Get Success")
    void test_IO_Success() {
        final String MOCK_NAME = "Emre";
        try {
            redisService.putSync("1", MOCK_NAME);
            String name = redisService.getSync("1");
            Assertions.assertEquals(MOCK_NAME, name);
        } catch (Exception e) {
            Assertions.fail("An error occurred while getting");
        }
    }

    @Test
    @DisplayName("Trying Put while redis unreachable")
    void test_IO_WrongConnection() {
        testRedisContainer.stop();
        Assertions.assertThrows(Throwable.class, () -> redisService.putSync("1", "Emre"));
    }
}
