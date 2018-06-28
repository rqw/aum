package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.Directive;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectiveParser {
    private  static final DirectiveParser PARSER=new DirectiveParser();
    private Map<String,Directive> directiveMap=new HashMap<>();
    private DirectiveParser(){
        directiveMap.put("fs",new FileSystemDirective());
    }
    public static DirectiveParser getInstance(){
        return PARSER;
    }
    public Directive getDirective(String name){
        return directiveMap.get(name);
    }
    public void registerDirective(String name,Directive directive){
        directiveMap.put(name,directive);
    }
    public boolean resolveExec(String commond){
        Pattern compile = Pattern.compile("([~@]*)@");
        Matcher matcher = compile.matcher(commond);
        String dname=null;
        if(matcher.find()) {
            dname = matcher.group(0);
            return directiveMap.get(dname).execute(commond);
        }else{
            throw new RuntimeException(String.format("cmd resolveExec fail,not support cmd:%s",commond));
        }
    }

}
