package com.shawn.seckill.config;

import com.shawn.seckill.pojo.User;

/**
 * @author : shawn
 * @date : 2022-08-05 16:41
 * @description :
 **/

public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

    public static void setUser(User user) {
        userHolder.set(user);
    }
    public static User getUser() {
        return userHolder.get();
    }
}

