package com.shawn.seckill.controller;


import com.shawn.seckill.pojo.User;
import com.shawn.seckill.service.IOrderService;
import com.shawn.seckill.vo.OrderDetailVo;
import com.shawn.seckill.vo.RespBean;
import com.shawn.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shawn
 * @since 2022-08-02
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    //订单详情
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user,Long orderId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detail=orderService.detail(orderId);
        return RespBean.success(detail);
    }

}
