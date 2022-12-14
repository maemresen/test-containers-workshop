package com.maemresen.tcw.with.annotation;

import com.maemresen.tcw.redis.RedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
class RedisServiceGetConnectionTest extends AbstractBaseRedisServiceTest {

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