package com.shawn.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawn.seckill.exception.GlobalException;
import com.shawn.seckill.mapper.UserMapper;
import com.shawn.seckill.pojo.User;
import com.shawn.seckill.service.IUserService;
import com.shawn.seckill.utils.CookieUtils;
import com.shawn.seckill.utils.MD5Util;
import com.shawn.seckill.utils.UUIDUtil;
import com.shawn.seckill.vo.LoginVo;
import com.shawn.seckill.vo.RespBean;
import com.shawn.seckill.vo.RespBeanEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shawn
 * @since 2022-07-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 登录
     * @param loginVo
     * @return
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //注释参数校验，判断自定义校验注解是否成功
//        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//
//        ///如果不是手机号
//        if(!ValidatorUtil.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }

        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        //如果查不到用户
        if(null == user){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //如果用户不为空，校验密码
        //传入的数据进行二次加密 与数据库的密码进行比较
        if(!MD5Util.fromPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
//        if(!MD5Util.inputPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            //返回用户名或者密码错误
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket= UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:"+ticket,user);

//        request.getSession().setAttribute(ticket,user);
        CookieUtils.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    //根据cookie获取用户
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user=(User) redisTemplate.opsForValue().get("user:"+userTicket);
        if(user!=null){
            CookieUtils.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }

    //更新用户密码,延时双删
    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if(user==null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        int result=userMapper.updateById(user);
        if(1==result){
            //删除redis
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }


}
