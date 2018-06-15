package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.ExecCmd;

public class UpgradeCmd implements ExecCmd {
    private String[] versions;
    public UpgradeCmd(String version){
        versions=version.split(",");
    }
    @Override
    public boolean exec() {
        return false;
    }
}
