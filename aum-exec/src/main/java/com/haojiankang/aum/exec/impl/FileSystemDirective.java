package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.tools.FileUtils;
import com.haojiankang.aum.tools.Strings;

import java.io.File;

public class FileSystemDirective extends AbstractDirecitve {
    {
        commondMap.put("cp",args->{
            if(args.length!=2)
                throw new RuntimeException("args length is error!");
            FileUtils.copyFile(new File(args[0]),new File(args[1]));
        });
        commondMap.put("rm",args->{
            if(args.length==0)
                throw new RuntimeException("args length is error!");
            for(String path:args){
                FileUtils.delFile(new File(path));
            }
        });
    }
    @Override
    public String prefix() {
        return "fs";
    }
}
