package com.haojiankang.aum.daemon.controller;

import com.haojiankang.aum.daemon.model.PackageInfo;
import com.haojiankang.aum.daemon.service.PackageInfoService;
import com.haojiankang.aum.daemon.model.SSTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/us/packageinfo")
@Api("应用程序升级包API")
@Slf4j
public class PackageinfoController {
    @Autowired
    private PackageInfoService service;
    @Autowired
    private Environment env;
    @ApiOperation(value = "上传应用程序升级包",notes="已经上传过的升级包会直接忽略。")
    @RequestMapping(method = RequestMethod.POST)
    public SSTO<?> upload(@ApiParam(name="file",value="升级包文件数据和信息",required=true)
            @RequestParam("file") MultipartFile file, PackageInfo info){
        try{
            service.save(info,file.getBytes());
        }catch(Exception e){
            log.error(e.getMessage(),e);
            return SSTO.not(e.getMessage());
        }
        return SSTO.ok("upload success!",info);
    }
    @ApiOperation(value = "触发应用升级",notes="会将指定程序已经上传的所有未执行的升级包按照顺序依次升级")
    @RequestMapping(value="upgrade/{appcode}/{pointcode}",method = RequestMethod.POST)
    public SSTO<?> upgrade(@ApiParam(name="appcode",value="应用代码",required=true)@PathVariable("appcode") String appcode,
                           @ApiParam(name="pointcode",value="节点代码",required=true)@PathVariable("pointcode") String pointcode){
        try{
            service.runUpdate(appcode,pointcode);
        }catch(Exception e){
            log.error(e.getMessage(),e);
            return SSTO.not(e.getMessage());
        }
        return SSTO.ok("upgrade success!");
    }
    @ApiOperation(value = "升级包信息列表",notes="查询全部升级包信息列表数据")
    @RequestMapping(method = RequestMethod.GET)
    public SSTO<?> list(){
        try{
            return SSTO.ok(null,service.listAll());
        }catch(Exception e){
            log.error(e.getMessage(),e);
            return SSTO.not(e.getMessage());
        }
    }
    @ApiOperation(value = "解锁",notes="")
    @RequestMapping(value="unlock",method = RequestMethod.GET)
    public SSTO<?> unlock(){
        try{
            service.UPDATE_LOCK=false;
            return SSTO.ok(null);
        }catch(Exception e){
            log.error(e.getMessage(),e);
            return SSTO.not(e.getMessage());
        }
    }

}
