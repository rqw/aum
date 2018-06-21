package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.ExecCmd;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;

public class UpgradeCmd implements ExecCmd {
    private String[] args;
    public UpgradeCmd(String args){
        this.args=args.split(",");
    }
    @Override
    public boolean exec() {
        for(String arg:args){
            //解压并验证安装包的完整性
            File sourceFile=new File(arg);
            ZipUtil.unexplode(sourceFile);

        }
        return false;
    }
}
