package com.haojiankang.aum.exec.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
public class ConnectionManager {
    @Data
    public static class Info {
        private String url;
        private String username;
        private String password;
        private String driverclass;
        private String dbtype;
    }

    public static Connection getConnection(Info info) {
        Connection conn = null;
        try {
            Class.forName(info.getDriverclass());
            switch (info.getDbtype()) {
                case "sqlite":
                    return DriverManager.getConnection(info.getUrl());
                case "oracle":
                default:
                    return DriverManager.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return conn;
    }
}
