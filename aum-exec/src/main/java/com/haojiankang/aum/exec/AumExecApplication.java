package com.haojiankang.aum.exec;

import com.haojiankang.aum.exec.api.Cmd;
import com.haojiankang.aum.exec.impl.UpdateCmd;
import com.haojiankang.aum.exec.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.xidea.el.ExpressionFactory;
import org.xidea.el.impl.ExpressionFactoryImpl;

import java.io.File;
import java.util.List;

@Slf4j
public class AumExecApplication {
    public static void main(String[] args){
        //args=new String[]{"update","D:\\run\\8059279e-898f-42d1-a5f8-eab6608de0b5.uuid"};
        try{
            resolve(args).exec();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
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
