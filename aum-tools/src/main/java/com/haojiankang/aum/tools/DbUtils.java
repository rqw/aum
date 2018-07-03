package com.haojiankang.aum.tools;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DbUtils {
    @Data
    public static class Info {
        private String url;
        private String username;
        private String password;
        private String driverclass;
        private String dbtype="oracle";
    }
    @Data
    public static class SqlScript{
        private String sql;
        private String type;
    }
    public static Connection getConnection(Info info) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Class.forName(info.getDriverclass());
        switch (info.getDbtype()) {
            case "sqlite":
                return DriverManager.getConnection(info.getUrl());
            case "oracle":
            default:
                return DriverManager.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
        }
    }
    public static Connection getConnection(String info) throws IOException,SQLException, ClassNotFoundException {
        Info jinfo = JsonUtils.parse(info, Info.class);
        return getConnection(jinfo);
    }

    public static List<SqlScript> resolve(List<String> sqlscript){
        List<SqlScript> list=new ArrayList<>();


        return list;
    }
    public static void execute(Connection connection,List<SqlScript> sqlList){

    }
}
