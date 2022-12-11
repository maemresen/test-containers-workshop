package com.maemresen.test.containers.workshop.redis;

import java.util.concurrent.ExecutionException;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RedisService redisService = new RedisService("localhost", 6379);
        UserService userService = new UserService(redisService);
        userService.save("1", "Emre");
        String name = userService.findById("1");
        System.out.println("Hello " + name + " you are successfully registered to the system");
    }

}
