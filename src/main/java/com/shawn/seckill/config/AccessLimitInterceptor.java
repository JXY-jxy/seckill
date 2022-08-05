package com.shawn.seckill.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shawn.seckill.pojo.User;
import com.shawn.seckill.service.IUserService;
import com.shawn.seckill.utils.CookieUtils;
import com.shawn.seckill.vo.RespBean;
import com.shawn.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
/**
 * @author : shawn
 * @date : 2022-08-05 16:30
 * @description :
 **/
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            User user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null) {
                return true;
            }
            int second = accessLimit.second();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key=request.getRequestURI();
            if(needLogin) {
                if(user == null) {
                    render(response, RespBeanEnum.SESSION_ERROR);
                    return false;
                }
                key = ":" + user.getId();
            }

            ValueOperations valueOperations = redisTemplate.opsForValue();
            //发起请求的地址，限制访问次数，
//            String uri = request.getRequestURI();
//            Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
            Integer count = (Integer) valueOperations.get(key);
            if(count == null) {
                valueOperations.set(key, 1, second, TimeUnit.SECONDS);
            } else if(count < maxCount) {
                valueOperations.increment(key);
            } else {
                render(response, RespBeanEnum.ACCESS_LIMIT_REAHED);
                return false;
            }
        }

        return true;
    }

    //构建返回对象
    private void render(HttpServletResponse response, RespBeanEnum error) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        RespBean respBean = RespBean.error(error);
        out.write(new ObjectMapper().writeValueAsString(respBean));
        out.flush();
        out.close();
    }

    //获取当前登录用户
    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String ticket = CookieUtils.getCookieValue(request, "userTicket");
        if(StringUtils.isEmpty(ticket)) {
            return null;
        }
        return userService.getUserByCookie(ticket,request,response);

    }

}
