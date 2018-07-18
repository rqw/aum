package com.haojiankang.aum.exec.api;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface GetDDL {
    enum DDLType{
        TABLE,FUNCTION,VIEW,TRIGGER,SEQUENCE,INDEX,PROCEDURE
    }
    void gain(Connection connection, DDLType type, OutputStream os, String... names)throws SQLException;;

}
