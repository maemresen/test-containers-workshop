package com.maemresen.tcw.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import java.util.concurrent.ExecutionException;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
public class RedisService {

    private final RedisClient redisClient;

    public RedisService(String host, Integer port) {
        this.redisClient = RedisClient.create(String.format("redis://%s:%d/%d", host, port, 0));
    }

    public StatefulRedisConnection<String, String> getConnection() {
        return this.redisClient.connect();
    }

    public String putSync(String id, String name) throws ExecutionException, InterruptedException {
        try (StatefulRedisConnection<String, String> connection = getConnection()) {
            RedisAsyncCommands<String, String> asyncCommand = connection.async();
            return asyncCommand.set(id, name).get();
        }
    }

    public String getSync(String id) throws ExecutionException, InterruptedException {
        try (StatefulRedisConnection<String, String> connection = getConnection()) {
            RedisAsyncCommands<String, String> asyncCommand = connection.async();
            return asyncCommand.get(id).get();
        }
    }
}
