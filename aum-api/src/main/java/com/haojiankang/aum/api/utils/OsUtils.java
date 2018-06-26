package com.haojiankang.aum.api.utils;

import java.lang.management.ManagementFactory;

public class OsUtils {
    public static String getPid(){
        String name=ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }

}
