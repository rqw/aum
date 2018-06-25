package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.Cmd;
import com.haojiankang.aum.exec.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.zeroturnaround.zip.ZipUtil;
import sun.reflect.annotation.ExceptionProxy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<File> listFile=new ArrayList<>();
        Map<String,String>  options=new HashMap<>();
        if(versions.length==1){
            unpackAndResolve(listFile,options,versions[0]);
        }else{
            for(String version:versions){
                unpackAndResolve(listFile, options, version);
            }
        }
        //根据安装文件解析结果执行安装
        //停止服务
        //执行安装
        //启动服务
        //修改当前应用程序版本信息

        return false;
    }

    private void unpackAndResolve(List<File> listFile, Map<String, String> options, String version) {
        try {
            File sourceFile = new File(pkgdir, String.format("%s%s%s%s%s", code, File.separator, point, File.separator, version));
            File tmpFile = new File(pkgdir, String.format("tmp%s%s_%s_%s", File.separator, code, point, version));
            //解压并验证安装包的完整性
            ZipUtil.unpack(sourceFile, tmpFile);
            String verify=FileUtils.readFileToString(new File(tmpFile,"sign.verify"), System.getProperty("file.encoding"));
            Pattern compile = Pattern.compile("md5:(.*),version:(.*),time:(.*)");
            Matcher matcher = compile.matcher(verify);
            String md5=null,ver=null,time=null;
            if(matcher.find()) {
                md5 = matcher.group(1);
                ver = matcher.group(2);
                time = matcher.group(3);
            }
            String fmd5=FileUtils.md5(new File(tmpFile,"data.zip"));
            if(!fmd5.equals(md5)||!version.equals(ver)){
                throw new RuntimeException("升级包文件验证未通过!");
            }
            listFile.add(tmpFile);
            options.put("server@start", "");
            //验证安装文件

            //解析安装文件

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

}
