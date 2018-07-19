package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.tools.DbUtils;
import com.haojiankang.aum.tools.FileUtils;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

public class DaemonService {
    private static DbUtils.Info SINFO=new DbUtils.Info(){{
        setDriverclass("org.sqlite.JDBC");
        setUrl("jdbc:sqlite:"+new File(FileUtils.getBasePath().getParent(),"us.db").getAbsolutePath());
    }};

    public static void markEnable(String code,String point) throws Exception{
        try(Connection conn = DbUtils.getConnection(SINFO);
            Statement statement = conn.createStatement();
        ){
            statement.executeUpdate(String.format("update appinfo set status='1' where appcode='%s' and pointcode='%s'",code,point));
        }
    }
    public static void markDisable(String code,String point) throws Exception{
        try(Connection conn = DbUtils.getConnection(SINFO);
            Statement statement = conn.createStatement();
        ){
            statement.executeUpdate(String.format("update appinfo set status='0' where appcode='%s' and pointcode='%s'",code,point));
        }
    }
    public static void changePkgState(String code,String point,String version,String state) throws Exception{
        try(Connection conn = DbUtils.getConnection(SINFO);
            Statement statement = conn.createStatement();
        ){
            statement.executeUpdate(String.format("update packageinfo set state='%s' where appcode='%s' and pointcode='%s' and version='%s'",state,code,point,version));
        }
    }
}
