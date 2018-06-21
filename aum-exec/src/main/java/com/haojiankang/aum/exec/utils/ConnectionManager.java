package com.haojiankang.aum.exec.utils;

import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
@Slf4j
public class ConnectionManager {
    public static Connection getConnection(String driver,String url,String userName,String password) {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, userName,password);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return conn;
    }
}
