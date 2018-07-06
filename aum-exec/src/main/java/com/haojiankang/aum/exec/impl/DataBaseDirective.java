package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.tools.DbUtils;
import com.haojiankang.aum.tools.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

public class DataBaseDirective   extends AbstractDirecitve  {
    {
        commondMap.put("imp",args->{
            if(args.length<2)
                throw new RuntimeException("imp args length is error!");
            try(Connection connection = DbUtils.getConnection(context.get(args[0]));){
                FileSystemResource rc = new FileSystemResource(new File(args[1]));
                EncodedResource er = new EncodedResource(rc, "utf-8");
                ScriptUtils.executeSqlScript(connection,er);
            }
        });
    }
    @Override
    public String prefix() {
        return "db";
    }

}
