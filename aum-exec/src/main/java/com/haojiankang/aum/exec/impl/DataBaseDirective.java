package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.tools.DbUtils;
import com.haojiankang.aum.tools.FileUtils;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

public class DataBaseDirective   extends AbstractDirecitve  {
    {
        commondMap.put("imp",args->{
            if(args.length<2)
                throw new RuntimeException("imp args length is error!");
            List<String> list = FileUtils.readFileToList(new File(args[1]), "utf-8");
            try(Connection connection = DbUtils.getConnection(context.get(args[0]));){
                List<DbUtils.SqlScript> sqls = DbUtils.resolve(list);
                DbUtils.execute(connection,sqls);
            }
        });
    }
    @Override
    public String prefix() {
        return "db";
    }

}
