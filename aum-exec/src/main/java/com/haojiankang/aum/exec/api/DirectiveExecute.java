package com.haojiankang.aum.exec.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
public interface DirectiveExecute {
    void execute(String[] args) throws Throwable;
}
