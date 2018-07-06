package com.haojiankang.aum.exec.api;
@FunctionalInterface
public interface Cmd {
    public boolean exec(String[] args);
}
