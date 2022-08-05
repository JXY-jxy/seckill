package com.shawn.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : shawn
 * @date : 2022-07-20 10:56
 * @description : 测试
 **/
@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("hello")
    public String hello(Model model){
        model.addAttribute("name","shawn");
        return "hello";
    }
}
