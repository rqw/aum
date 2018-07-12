package com.haojiankang.aum.daemon.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class IndexController {

    @ApiIgnore
    @RequestMapping(value="/",method = RequestMethod.GET)
    public String index(){
        return "redirect:swagger-ui.html";
    }
    @ApiOperation(value = "验证接口",notes="用于供控制中心验证服务是否可用，返回success")
    @RequestMapping(value="/verify",method = RequestMethod.GET)
    @ResponseBody
    public String verify(){
        return "success";
    }
}
