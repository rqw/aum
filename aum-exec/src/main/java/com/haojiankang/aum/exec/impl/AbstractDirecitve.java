package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.Directive;
import com.haojiankang.aum.exec.api.DirectiveExecute;
import com.haojiankang.aum.tools.FileUtils;
import com.haojiankang.aum.tools.Strings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDirecitve implements Directive {
    public abstract String prefix();
    protected  Map<String,String> context=new HashMap<>();
    public void changeContenxt(Map<String,String> context){
        this.context=context;
    }
    protected Map<String,DirectiveExecute> commondMap=new HashMap<>();
    public void register(String cmd,DirectiveExecute cmdFun){
        commondMap.put(cmd,cmdFun);
    }
    @Override
    public boolean execute(String argline) {
        try{
            return resolveAndExec(argline);
        }catch(Throwable e){
            throw new RuntimeException(e);
        }
    }

    protected boolean  resolveAndExec(String argline) throws Throwable{
        Pattern compile = Pattern.compile(prefix()+"@(\\S*)\\s+(.*)");
        Matcher matcher = compile.matcher(argline);
        String commond,args;
        if(matcher.find()){
            commond=matcher.group(1);
            args=matcher.group(2);
            executeCommand(commond,args);
        }else{
            throw new RuntimeException(prefix()+"@Directive args is not matcher!");
        }
        return true;
    }
    protected void executeCommand(String commond,String arg) throws Throwable{
        String[] args = resolveArgs(arg);
        DirectiveExecute cmd = commondMap.get(commond);
        if(cmd==null)
            throw new RuntimeException(String.format(prefix()+" commond '%s' is not found!",commond));
        cmd.execute(args);

    }

    protected String[] resolveArgs(String args){
        return Strings.compile(args,context).split(" ");
    }
}
