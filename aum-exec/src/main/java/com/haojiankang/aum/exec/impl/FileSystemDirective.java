package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.tools.FileUtils;
import com.haojiankang.aum.tools.Strings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class FileSystemDirective extends AbstractDirecitve {
    {
        commondMap.put("cp",args->{
            if(args.length!=2)
                throw new RuntimeException("args length is error!");
            try(
                 FileOutputStream fos = new FileOutputStream(new File(context.get("bak.dir"),"fs.list"),true);
                 OutputStreamWriter fr = new OutputStreamWriter(fos, "utf-8");
                 ){
                FileUtils.copyFile(new File(args[0]),new File(args[1]),new File(context.get("bak.dir"),"fs"),fr);
            }
        });
        commondMap.put("rm",args->{
            if(args.length==0)
                throw new RuntimeException("args length is error!");
            try(
                    FileOutputStream fos = new FileOutputStream(new File(context.get("bak.dir"),"fs.list"),true);
                    OutputStreamWriter fr = new OutputStreamWriter(fos, "utf-8");
                    ){
                for(String path:args){
                    File delFile=new File(path);
                    File backDir=new File(context.get("bak.dir"),"fs");
                    FileUtils.delFile(delFile,new File(backDir,delFile.getName()),fr);
                }
            }
        });
    }
    @Override
    public String prefix() {
        return "fs";
    }
}
