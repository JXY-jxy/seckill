package com.shawn.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shawn.seckill.pojo.User;
import com.shawn.seckill.vo.LoginVo;
import com.shawn.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shawn
 * @since 2022-07-20
 */
public interface IUserService extends IService<User> {

    //登录
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    //根据cookie获取用户
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

    //更改用户密码
    RespBean updatePassword(String userTicket,String password, HttpServletRequest request, HttpServletResponse response);

}
