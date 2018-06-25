package com.haojiankang.aum.exec;

import com.haojiankang.aum.exec.api.Cmd;
import com.haojiankang.aum.exec.impl.UpdateCmd;
import com.haojiankang.aum.exec.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class AumExecApplication {
    public static void main(String[] args){
        args=new String[]{"update","D:\\run\\7a31b0a6-b084-4bef-9894-36c0a9625629.uuid"};
        resolve(args).exec();
    }
    private static Cmd resolve(String[] args){
        if(args.length==0){
            throw new RuntimeException("args length is zero!");
        }
        Cmd cmd=null;
        switch(args[0]){
            case "update":
                cmd=new UpdateCmd(args);
                break;
        }
        return cmd;


    }
}
