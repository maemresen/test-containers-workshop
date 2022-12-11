package com.maemresen.test.containers.workshop.redis;

import com.github.dockerjava.api.command.CreateContainerCmd;
import io.lettuce.core.api.StatefulRedisConnection;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
class RedisService_ConnectionTest {

    final GenericContainer testRedisContainer;

    RedisService_ConnectionTest() {
        DockerImageName dockerImageName = DockerImageName.parse("redis:7.0.5-alpine");
        this.testRedisContainer = new GenericContainer(dockerImageName)
            .withCreateContainerCmdModifier((Consumer<CreateContainerCmd>) cmd -> cmd.withName("TEST-REDIS"))
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());
    }

    @BeforeEach
    void setUp() {
        testRedisContainer.start();
        System.out.printf("Starting::%s:%s%n", testRedisContainer.getContainerId(), testRedisContainer.getContainerName());
    }

    @AfterEach
    void wrapUp() {
        System.out.printf("Stopping::%s:%s%n", testRedisContainer.getContainerId(), testRedisContainer.getContainerName());
        testRedisContainer.stop();
    }

    @Test
    void test_GetConnection_RedisAccessible() {
        System.out.println("CASE::test_GetConnection_RedisAccessible");
        String testRedisHost = testRedisContainer.getHost();
        Integer testRedisPort = testRedisContainer.getMappedPort(6379);
        String connectionString = String.format("redis://%s:%d/%d", testRedisHost, testRedisPort, 0);
        RedisService redisService = new RedisService(connectionString);
        StatefulRedisConnection<String, String> connection;
        try {
            connection = redisService.getConnection();
        } catch (Exception exception) {
            exception.printStackTrace();
            connection = null;
        }
        Assertions.assertNotNull(connection);
        Assertions.assertDoesNotThrow(() -> {
            try(StatefulRedisConnection<String, String> conn = redisService.getConnection()) {
            }
        });
    }

    @Test
    void test_GetConnection_WrongConnectionString() {
        System.out.println("CASE::test_GetConnection_RedisAccessible");
        String testRedisHost = "wrongHost";
        Integer testRedisPort = testRedisContainer.getMappedPort(6379);
        String connectionString = String.format("redis://%s:%d/%d", testRedisHost, testRedisPort, 0);
        RedisService redisService = new RedisService(connectionString);
        Assertions.assertThrows(Throwable.class, redisService::getConnection);
    }
}