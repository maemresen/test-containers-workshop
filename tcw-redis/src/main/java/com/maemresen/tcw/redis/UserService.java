package com.maemresen.tcw.redis;

import java.util.concurrent.ExecutionException;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
public class UserService {

    private final RedisService redisService;

    public UserService(RedisService redisService) {
        this.redisService = redisService;
    }

    public String save(String id, String name) throws ExecutionException, InterruptedException {
        return redisService.putSync(id, name);
    }

    public String findById(String id) throws ExecutionException, InterruptedException {
        return redisService.getSync(id);
    }
}
