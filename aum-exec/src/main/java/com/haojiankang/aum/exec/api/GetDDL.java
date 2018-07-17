package com.haojiankang.aum.exec.api;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface GetDDL {
    enum DDLType{
        TABLE,FUNCTION,VIEW,TRIGGER,SEQUENCE,INDEX,PROCEDURE
    }
    String gain(Connection connection,DDLType type, String... names)throws SQLException;;

}
