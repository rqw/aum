package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.Cmd;
import com.haojiankang.aum.tools.FileUtils;
import com.haojiankang.aum.tools.JsonUtils;
import com.haojiankang.aum.tools.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class UpdateCmd implements Cmd {
    private String code;
    private String point;
    private String basedir;
    private String pkgdir;
    private String properties;
    private Map<String,String> env;
    private String[] versions;
    private void resolve(String[] args) {
        env=new HashMap<>();
        if(args.length!=2&&args[1]==null){
            throw new RuntimeException("updatecmd args fail!");
        }
        try{
            String content = FileUtils.readFileToString(new File(args[1]), System.getProperty("file.encoding"));
            Pattern compile = Pattern.compile("code:(.*),point:(.*),basedir:(.*),properties:([.\r\n]*),version:(.*)");
            Matcher matcher = compile.matcher(content);
            if(matcher.find()){
                code=matcher.group(1);
                point=matcher.group(2);
                basedir=matcher.group(3);
                pkgdir=String.format("%s%s%s",basedir,File.separator,"package");
                properties=matcher.group(4);
                env=JsonUtils.parse(properties,env.getClass());
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
    public boolean exec(String[] args)  {
        try{
            resolve(args);
            List<File> listFile=new ArrayList<>();
            if(versions.length==1){
                unpackAndResolve(listFile,versions[0]);
            }else{
                for(String version:versions){
                    unpackAndResolve(listFile, version);
                }
            }
            //停止服务
            if(env.get("shutdown")!=null&&env.get("shutdown").trim().length()>0){
                ProcessUtils.exec(env.get("shutdown"));
            }else{
                ProcessUtils.kill(env.get("pid"));
            }
            //执行安装
            batchCmd(listFile);
            //启动服务
            ProcessUtils.exec(env.get("startup"));
            //删除参数文件
            new File(args[1]).delete();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            //升级过程中发生异常，记录异常日志文件，并通知daemon更新升级包的相关信息
        }
        return false;
    }

    public void batchCmd(List<File> listFile) throws IOException {
        //修改上下文
        DirectiveParser.changeContext(env);

        for(File dataFile:listFile){
            env.put("pkg.dir",new File(dataFile,"data").getAbsolutePath());
            File cmdFile=new File(dataFile,"data"+File.separator+"command.list");
            if(cmdFile.exists()){
                List<String> cmdlist = FileUtils.readFileToList(cmdFile, "utf-8");
                for(String cmd:cmdlist){
                    DirectiveParser.getInstance().resolveExec(cmd);
                }
            }else{
                throw new RuntimeException(String.format("cmdfile is not exists,this path:%s",cmdFile.getAbsolutePath()));
            }
        }
    }


    private void unpackAndResolve(List<File> listFile, String version) {
        try {
            File sourceFile = new File(pkgdir, String.format("%s%s%s%s%s", code, File.separator, point, File.separator, version));
            File tmpFile = new File(pkgdir, String.format("tmp%s%s_%s_%s", File.separator, code, point, version));
            //解压并验证安装包的完整性
            ZipUtil.unpack(sourceFile, tmpFile);
            String verify=FileUtils.readFileToString(new File(tmpFile,"sign.verify"),"utf-8");
            Pattern compile = Pattern.compile("md5:(.*),version:(.*),time:(.*)");
            Matcher matcher = compile.matcher(verify);
            String md5=null,ver=null,time=null;
            if(matcher.find()) {
                md5 = matcher.group(1);
                ver = matcher.group(2);
                time = matcher.group(3);
            }

            //验证安装文件
            String fmd5=FileUtils.md5(new File(tmpFile,"data.zip"));
            if(!fmd5.equals(md5)||!version.equals(ver)){
                throw new RuntimeException("升级包文件验证未通过!");
            }
            listFile.add(tmpFile);
            //解压data.zip
            ZipUtil.unpack(new File(tmpFile,"data.zip"),tmpFile);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

}
