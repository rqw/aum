package com.haojiankang.aum.daemon.controller;

import com.haojiankang.aum.daemon.po.AppInfo;
import com.haojiankang.aum.daemon.service.AppinfoService;
import com.haojiankang.aum.daemon.utils.SSTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/us/appinfo")
@Api("应用信息API")
@Slf4j
public class AppinfoController {
    @Autowired
    private AppinfoService service;
    @ApiOperation(value = "注册应用信息",notes="根据appcode和pointcode确定应用是否已经注册，如果已经注册会更新应用相关信息。")
    @RequestMapping(method = RequestMethod.POST)
    public SSTO<?> register(AppInfo info){
        try{
            service.register(info);
        }catch(Exception e){
            log.error(e.getMessage(),e);
            return SSTO.not(e.getMessage());
        }
        return SSTO.ok("register success!",info);
    }
}
