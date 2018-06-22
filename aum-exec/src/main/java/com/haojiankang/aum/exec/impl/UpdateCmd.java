package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.Cmd;
import com.haojiankang.aum.exec.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class UpdateCmd implements Cmd {
    private String[] args;
    private String code;
    private String point;
    private String pkgdir;
    private String properties;
    private String[] versions;
    public UpdateCmd(String[] args){
        this.args=args;
        resolve(args);
    }

    private void resolve(String[] args) {
        if(args.length!=2&&args[1]==null){
            throw new RuntimeException("updatecmd args fail!");
        }
        try{
            String content = FileUtils.readFileToString(new File(args[1]), System.getProperty("file.encoding"));
            Pattern compile = Pattern.compile("code:(.*),point:(.*),pkgdir:(.*),properties:(.*),version:(.*)");
            Matcher matcher = compile.matcher(content);
            if(matcher.find()){
                code=matcher.group(1);
                point=matcher.group(2);
                pkgdir=matcher.group(3);
                properties=matcher.group(4);
                versions=matcher.group(5).split(",");
            }else{
                throw new RuntimeException("updatecmd args file content is fail!");
            }
            log.debug("code:{},point:{},pkgdir:{},properties:{},version:{}",code,point,pkgdir,properties,versions);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public boolean exec() {
        if(versions.length==1){

        }else{
            for(String version:versions){
                //解压并验证安装包的完整性
                File sourceFile=new File(pkgdir,String.format("%s%s%s%s%s",code,File.separator,point,File.separator,version));
                ZipUtil.unexplode(sourceFile);
                //验证安装文件
                //解析安装文件

            }
        }
        //根据安装文件解析结果执行安装
        //停止服务
        //执行安装
        //启动服务
        //修改当前应用程序版本信息

        return false;
    }

}
