package com.haojiankang.aum.exec;

import com.haojiankang.aum.exec.impl.DirectiveParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DirectiveParserTest {
    @Test
    public void testDbExp(){
        Map<String,String> content=new HashMap<>();
        content.put("hid","{'username':'hid','password':'hid','driverclass':'oracle.jdbc.driver.OracleDriver','url':'jdbc:oracle:thin:@192.168.191.51:1521:orcl'}");
        content.put("bak.dir","D:\\BACK");
        DirectiveParser.getInstance().changeContext(content);
        DirectiveParser.getInstance().resolveExec("db@exp hid TABLE HJK_SYS_USER");
        DirectiveParser.getInstance().resolveExec("db@exp hid DATA HJK_SYS_USER");
        DirectiveParser.getInstance().resolveExec("db@exp hid TABLE HJK_SYS_ORGANIZATION");
    }
    @Test
    public void testFs(){
        Map<String,String> content=new HashMap<>();
        content.put("bak.dir","D:\\BACK");
        DirectiveParser.getInstance().changeContext(content);
        DirectiveParser.getInstance().resolveExec("fs@rm D:\\zentao");
        DirectiveParser.getInstance().resolveExec("fs@cp D:\\run\\package\\tmp D:\\BACK2");
    }
}
