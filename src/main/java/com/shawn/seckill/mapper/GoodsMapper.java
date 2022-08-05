package com.shawn.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shawn.seckill.pojo.Goods;
import com.shawn.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shawn
 * @since 2022-08-02
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    //获取商品列表
    List<GoodsVo> findGoodsVo();

    //获取商品详情
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
