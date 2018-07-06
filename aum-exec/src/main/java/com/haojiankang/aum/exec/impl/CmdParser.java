package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.Cmd;

import java.util.HashMap;
import java.util.Map;

public class CmdParser {
    private static CmdParser PARSER=new CmdParser();
    private Map<String,Cmd> commandMap=new HashMap<>();
    {
        commandMap.put("update",new UpdateCmd());
    }
    private CmdParser(){

    }
    public void execute(String[] args){
        if(args.length==0){
            throw new RuntimeException("args length is zero!");
        }
        Cmd command = commandMap.get(args[0]);
       if(command==null){
           throw new RuntimeException(String.format("%s is not commond!",args[0]));
       }
       command.exec(args);
    }
    public static CmdParser getInstance(){
       return PARSER;
    }


}
