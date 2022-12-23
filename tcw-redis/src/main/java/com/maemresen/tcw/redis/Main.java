package com.maemresen.tcw.redis;

import java.util.concurrent.ExecutionException;
import lombok.extern.java.Log;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
@Log
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RedisService redisService = new RedisService("localhost", 6379);
        UserService userService = new UserService(redisService);
        userService.save("1", "Emre");
        String name = userService.findById("1");
        log.info("Hello " + name + " you are successfully registered to the system");
    }

}
