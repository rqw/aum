package com.haojiankang.aum.tools;

import java.util.HashMap;
import java.util.Map;

public class Maps {
    public static <K,V> Map<K,V> newHMap(K k, V v){
        Map map=new HashMap();
        map.put(k,v);
        return map;
    }
}
