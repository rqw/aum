package com.haojiankang.aum.exec;

import com.haojiankang.aum.exec.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
@Slf4j
public class AumExecApplication {
    public static void main(String[] args){
        resolve(args);
    }
    private static void resolve(String[] args){
        if(args.length==0){
            throw new RuntimeException("args length is zero!");
        }
        try{
            FileUtils.readFileToList(new File(args[0]),System.getProperty("file.encoding"));
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }

    }
}
