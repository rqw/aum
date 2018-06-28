package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.Directive;
import com.haojiankang.aum.exec.utils.JsonUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystemDirective implements Directive {
    @Override
    public boolean execute(String argline) {
        return resolveAndExec(argline);
    }
    private boolean  resolveAndExec(String argline){
        Pattern compile = Pattern.compile("fs@(.*)\\s(.*)");
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
    private boolean executeCommand(String commond,String arg){
        String[] args = resolveArgs(arg);
        switch(commond){
            case "":

        }
        return false;
    }
    private String[] resolveArgs(String args){
        return args.split(" ");
    }
}
