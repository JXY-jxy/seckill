package com.shawn.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shawn.seckill.pojo.SeckillOrder;
import com.shawn.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shawn
 * @since 2022-08-02
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    //获取秒杀结果
    //orderId:成功 ，-1秒杀失败，0排队中
    Long getResult(User user, Long goodsId);
}
