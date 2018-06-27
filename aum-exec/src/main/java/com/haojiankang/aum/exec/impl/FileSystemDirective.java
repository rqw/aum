package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.Directive;

public class FileSystemDirective implements Directive {
    @Override
    public boolean execute(String argline) {
        return resolveAndExec(argline);
    }
    private boolean  resolveAndExec(String argline){

        return false;
    }
}
