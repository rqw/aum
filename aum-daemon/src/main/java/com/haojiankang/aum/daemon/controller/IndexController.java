package com.haojiankang.aum.daemon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class IndexController {
    @RequestMapping(value="/",method = RequestMethod.GET)
    public String index(){
        return "redirect:swagger-ui.html";
    }
}
