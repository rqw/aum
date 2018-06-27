package com.haojiankang.aum.exec.api;
@FunctionalInterface
public interface Directive {
    boolean execute(String argline);
}
