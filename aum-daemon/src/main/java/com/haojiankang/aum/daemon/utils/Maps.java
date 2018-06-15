package com.haojiankang.aum.daemon.utils;

import java.util.Map;

public class Maps {
    public static <K,V> Map<K,V> newHMap(K k, V v){
        Map map=com.google.common.collect.Maps.newHashMap();
        map.put(k,v);
        return map;
    }
}
