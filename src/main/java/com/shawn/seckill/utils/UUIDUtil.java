package com.shawn.seckill.utils;


import java.util.UUID;
/**
 * @author : shawn
 * @date : 2022-08-02 12:17
 * @description :
 **/
public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
