package com.maemresen.test.containers.workshop.redis.container.nonshared.annotation.without;

import com.maemresen.test.containers.workshop.redis.RedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
class RedisService_GetConnectionContainerNonSharedWithoutAnnotationTest extends AbstractBaseRedisServiceContainerNonSharedWithoutAnnotationTest {

    @Test
    void test_RedisReachable() {
        RedisService redisService = new RedisService(getTestRedisContainerHost(), getTestRedisContainerPort());
        Assertions.assertDoesNotThrow(redisService::getConnection);
    }

    @Test
    void test_WrongConnectionInfo() {
        RedisService redisService = new RedisService("wrongHost", getTestRedisContainerPort());
        Assertions.assertThrows(Throwable.class, redisService::getConnection);
    }

    @Test
    void test_HostUnreachable() {
        RedisService redisService = new RedisService(getTestRedisContainerHost(), getTestRedisContainerPort());
        testRedisContainer.stop();
        Assertions.assertThrows(Throwable.class, redisService::getConnection);
    }
}