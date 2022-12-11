package com.maemresen.test.containers.workshop.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
class RedisService_PutTest extends AbstractBaseRedisServiceTest {

    @Test
    @DisplayName("Put Success")
    void test_Put_Success() {
        final String MOCK_NAME = "Emre";
        try {
            RedisService redisService = getRedisService(getTestRedisContainerHost(), getTestRedisContainerPort());
            redisService.putSync("1", MOCK_NAME);
            String name = redisService.getSync("1");
            Assertions.assertEquals(MOCK_NAME, name);
        } catch (Exception e) {
            Assertions.fail("An error occurred while getting");
        }
    }

    @Test
    @DisplayName("Trying Put while redis unreachable")
    void test_Put_WrongConnection() {
        System.out.println("CASE::test_Put_RedisUnreachable");
        RedisService redisService = getRedisService(getTestRedisContainerHost(), getTestRedisContainerPort());
        testRedisContainer.stop();
        Assertions.assertThrows(Throwable.class, () -> redisService.putSync("1", "Emre"));
    }
}
