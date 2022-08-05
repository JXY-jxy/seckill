package com.shawn.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shawn.seckill.pojo.Goods;
import com.shawn.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shawn
 * @since 2022-08-02
 */
public interface IGoodsService extends IService<Goods> {

    //获取商品列表
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
