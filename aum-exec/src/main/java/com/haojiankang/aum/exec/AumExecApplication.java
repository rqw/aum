package com.haojiankang.aum.exec;

import com.haojiankang.aum.exec.api.Cmd;
import com.haojiankang.aum.exec.impl.CmdParser;
import com.haojiankang.aum.exec.impl.UpdateCmd;
import lombok.extern.slf4j.Slf4j;
import org.xidea.el.ExpressionFactory;
import org.xidea.el.impl.ExpressionFactoryImpl;

import java.util.Arrays;

@Slf4j
public class AumExecApplication {
    public static void main(String[] args){
        log.debug("args is :{}",String.join(",", Arrays.asList(args)));
        CmdParser.getInstance().execute(args);
    }
}
