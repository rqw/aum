package com.haojiankang.aum.exec.api;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface GetDML {
    void gain(Connection connection, OutputStream os, String... names)throws SQLException;;
}
