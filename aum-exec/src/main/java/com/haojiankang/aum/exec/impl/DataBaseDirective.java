package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.tools.DbUtils;
import com.haojiankang.aum.tools.FileUtils;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;

public class DataBaseDirective   extends AbstractDirecitve  {
    {
        commondMap.put("imp",args->{
            if(args.length<2)
                throw new RuntimeException("imp args length is error!");
            String sql= FileUtils.readFileToString(new File(args[1]), "utf-8");

            try(Connection connection = DbUtils.getConnection(context.get(args[0]));
                CallableStatement callableStatement = connection.prepareCall(sql);){
                callableStatement.execute();
            }
        });
    }
    @Override
    public String prefix() {
        return "db";
    }

}
