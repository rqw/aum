package com.haojiankang.aum.exec.utils;

import java.io.IOException;

public class ProcessUtils {
    private static final String KILL_CMD_WIN="taskkill /F /PID %s";
    public static void kill(String pid) throws IOException {
        Runtime.getRuntime().exec(String.format(KILL_CMD_WIN,pid));
    }
    public static void exec(String cmd)  throws IOException{
        Runtime.getRuntime().exec(cmd);
    }
}
