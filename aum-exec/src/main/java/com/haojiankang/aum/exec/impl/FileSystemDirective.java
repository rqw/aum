package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.Directive;
import com.haojiankang.aum.tools.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystemDirective implements Directive {
    @FunctionalInterface
    public static interface Commond{
        void execute(String[] args) throws IOException;
    }
    private Map<String,Commond> commondMap=new HashMap<>();
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
    public void register(String cmd,Commond cmdFun){
        commondMap.put(cmd,cmdFun);
    }
    @Override
    public boolean execute(String argline) {
        try{
            return resolveAndExec(argline);
        }catch(IOException e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }
    private boolean  resolveAndExec(String argline) throws IOException{
        Pattern compile = Pattern.compile("fs@(.*)\\s+(.*)");
        Matcher matcher = compile.matcher(argline);
        String commond,args;
        if(matcher.find()){
            commond=matcher.group(1);
            args=matcher.group(2);
        }else{
            throw new RuntimeException("fs@Directive args is not matcher!");
        }
        return false;
    }
    private void executeCommand(String commond,String arg) throws IOException{
        String[] args = resolveArgs(arg);
        Commond cmd = commondMap.get(commond);
        if(cmd==null)
            throw new RuntimeException(String.format("fs commond '%s' is not found!",commond));
        cmd.execute(args);

    }
    private String[] resolveArgs(String args){
        return args.split(" ");
    }
}
