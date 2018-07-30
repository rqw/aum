package com.haojiankang.aum.exec;

import com.haojiankang.aum.exec.impl.DirectiveParser;
import com.haojiankang.aum.tools.FileUtils;
import org.junit.Test;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DirectiveParserTest {
    @Test
    public void testDbExp() {
        Map<String, String> content = new HashMap<>();
        content.put("hid", "{'username':'hid','password':'hid','driverclass':'oracle.jdbc.driver.OracleDriver','url':'jdbc:oracle:thin:@192.168.191.51:1521:orcl'}");
        content.put("bak.dir", "D:\\BACK");
        DirectiveParser.getInstance().changeContext(content);
        DirectiveParser.getInstance().resolveExec("db@exp hid TABLE HJK_SYS_USER");
        DirectiveParser.getInstance().resolveExec("db@exp hid DATA HJK_SYS_USER");
        DirectiveParser.getInstance().resolveExec("db@exp hid TABLE HJK_SYS_ORGANIZATION");
    }

    @Test
    public void testFs() {
        Map<String, String> content = new HashMap<>();
        content.put("bak.dir", "D:\\BACK");
        DirectiveParser.getInstance().changeContext(content);
        DirectiveParser.getInstance().resolveExec("fs@rm D:\\zentao");
        DirectiveParser.getInstance().resolveExec("fs@cp D:\\run\\package\\tmp D:\\BACK2");
    }

    @Test
    public void testCreatePkg() throws Exception {
        List<String> list = Arrays.asList("1.0.1",
                "1.0.2",
                "1.0.3",
                "1.0.4",
                "1.0.5",
                "1.0.6",
                "1.0.7",
                "1.0.8",
                "1.0.9",
                "1.0.10"
        );
        for (String version : list) {
            createPkg(version);
        }
    }

    private void createPkg(String version) throws IOException, NoSuchAlgorithmException {
        File dir = new File("D:\\update");
        File signFile = new File(dir, "sign.verify");
        File pack = new File(dir, version + ".zip");
        File dataFile = new File(dir, "data.zip");
        ZipUtil.pack(new File(dir, version + File.separator + "data"), dataFile);
        String md5 = FileUtils.md5(dataFile);
        FileUtils.writeFile(String.format("md5:%s,version:%s,time:%s", md5, version, new Date().toLocaleString()), signFile);
        ZipUtil.packEntries(new File[]{dataFile, signFile}, pack);
        dataFile.delete();
        signFile.delete();
    }
}
