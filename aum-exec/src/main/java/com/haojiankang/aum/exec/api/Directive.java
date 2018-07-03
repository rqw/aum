package com.haojiankang.aum.exec.api;

import java.util.HashMap;
import java.util.Map;


public interface Directive {
    boolean execute(String argline);
    void changeContenxt(Map<String,String> context);
}
