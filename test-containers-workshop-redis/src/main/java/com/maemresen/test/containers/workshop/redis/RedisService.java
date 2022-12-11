package com.maemresen.test.containers.workshop.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import java.util.concurrent.ExecutionException;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
public class RedisService {

    private final RedisClient redisClient;

    public RedisService(String connectionString) {
        this.redisClient = RedisClient.create(connectionString);
    }

    public StatefulRedisConnection<String, String> getConnection() {
        return this.redisClient.connect();
    }

    private RedisAsyncCommands<String, String> getAsyncCommand() {
        return getConnection().async();
    }

    public String putSync(String id, String name) throws ExecutionException, InterruptedException {
        RedisAsyncCommands<String, String> asyncCommand = getAsyncCommand();
        return asyncCommand.set(id, name).get();
    }

    public String getSync(String id) throws ExecutionException, InterruptedException {
        RedisAsyncCommands<String, String> asyncCommand = getAsyncCommand();
        return asyncCommand.get(id).get();
    }
}
