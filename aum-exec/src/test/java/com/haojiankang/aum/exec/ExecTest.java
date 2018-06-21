package com.haojiankang.aum.exec;

import com.haojiankang.aum.exec.utils.ConnectionManager;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

public class ExecTest {
    @Test
    public void testConnection(){
        Connection connection=ConnectionManager.getConnection("oracle.jdbc.OracleDriver","jdbc:oracle:thin:@//47.95.144.189:1521/orcl","hidbasic","hidbasic");
        Assert.assertNotNull(connection);
    }
}
