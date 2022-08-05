package com.shawn.seckill.controller;

import com.shawn.seckill.pojo.User;
import com.shawn.seckill.service.IGoodsService;
import com.shawn.seckill.service.IUserService;
import com.shawn.seckill.vo.DetailVo;
import com.shawn.seckill.vo.GoodsVo;
import com.shawn.seckill.vo.RespBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;
/*
windows 优化前 qps 776.2
        优化后      1491
linux   优化前 qps 540.7
 */

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 跳转到商品列表页
     */
    @RequestMapping(value="/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        //Redis中获取页面，若不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
//        return "goodsList";
        //如果为空，手动渲染，存入Redis并返回
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);

        }
        return html;
    }

    /**
     * 跳转到商品详情页
     */
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, User user, @PathVariable Long goodsId){

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate=goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate=new Date();
        //秒杀状态
        int secKillStatus=0;
        //秒杀倒计时
        int remainSeconds=0;
        //秒杀还未开始
        if(nowDate.before(startDate)){
            remainSeconds=(int) (startDate.getTime()-nowDate.getTime())/1000;
        }else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus=2;
            remainSeconds=-1;
        }else{
            //秒杀进行中
            secKillStatus=1;
            remainSeconds=0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);

        return RespBean.success(detailVo);

    }

    @RequestMapping(value="/toDetail2/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response){

        //Redis中获取页面，若不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:"+goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate=goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate=new Date();
        //秒杀状态
        int secKillStatus=0;
        //秒杀倒计时
        int remainSeconds=0;
        //秒杀还未开始
        if(nowDate.before(startDate)){
            remainSeconds=(int) (startDate.getTime()-nowDate.getTime())/1000;
        }else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus=2;
            remainSeconds=-1;
        }else{
            //秒杀进行中
            secKillStatus=1;
            remainSeconds=0;
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("goods",goodsVo);
        //如果为空，手动渲染，存入Redis并返回
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail"+goodsId,html,60, TimeUnit.SECONDS);

        }
        return html;

    }


}
