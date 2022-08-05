package com.shawn.seckill.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码校验
 */
public class ValidatorUtil {

    //正则表达式校验
    private static final Pattern mobile_pattern = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");

    public static boolean isMobile(String mobile){
        if (StringUtils.isEmpty(mobile)){
            return false;
        }

        //matches 匹配   传入的mobile  进行校验  递归
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
