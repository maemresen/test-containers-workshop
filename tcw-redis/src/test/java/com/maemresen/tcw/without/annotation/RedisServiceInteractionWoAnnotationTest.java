package com.maemresen.tcw.without.annotation;

import com.maemresen.tcw.redis.RedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
class RedisServiceInteractionWoAnnotationTest extends AbstractBaseRedisServiceWoAnnotationTest {

    RedisService redisService;

    @BeforeEach
    void setUp(){
        redisService = new RedisService(getTestRedisContainerHost(), getTestRedisContainerPort());
    }

    @Test
    void test_Success() {
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
    void test_HostUnreachable() {
        testRedisContainer.stop();
        Assertions.assertThrows(Throwable.class, () -> redisService.putSync("1", "Emre"));
    }
}
