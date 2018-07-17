package com.haojiankang.aum.exec.impl;

import com.haojiankang.aum.exec.api.GetDDL;
import com.haojiankang.aum.exec.api.GetDML;
import com.haojiankang.aum.tools.DbUtils;
import com.haojiankang.aum.tools.FileUtils;
import com.haojiankang.aum.tools.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.Map;
import java.io.File;
import java.util.HashMap;
import java.util.List;
@Slf4j
public class DataBaseDirective   extends AbstractDirecitve  {
    Map<String,GetDDL> expddls=new HashMap<>();
    Map<String,GetDML> expdmls=new HashMap<>();
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
        commondMap.put("exp",args->{
            //dbname type names...
            if(args.length<3)
                throw new RuntimeException("imp args length is error!");
            DbUtils.Info jinfo = JsonUtils.parse(context.get(args[0]), DbUtils.Info.class);
            GetDDL ddl = expddls.get(jinfo.getDbtype());
            if(ddl==null){
                throw new RuntimeException(String.format("exp database type:%s not support!",jinfo.getDbtype()));
            }
            try(Connection connection = DbUtils.getConnection(jinfo);){
                String ddlString = ddl.gain(connection,GetDDL.DDLType.valueOf(args[1]), args[2].split(","));
                log.debug(ddlString);
            }


        });
        expddls.put("oracle",(conn,type,args)->{
            StringBuilder sb=new StringBuilder();
            try(
                PreparedStatement pst = conn.prepareStatement(String.format("SELECT DBMS_METADATA.GET_DDL(U.OBJECT_TYPE, U.object_name)  FROM USER_OBJECTS U  where U.OBJECT_TYPE = '%s' AND   U.object_name IN('%s')", type.name(), String.join("','", args)));
                ResultSet rst = pst.executeQuery();
            ){
               while(rst.next()){
                   Clob clob = rst.getClob(1);
                   sb.append(clob.getSubString(1L,(int)clob.length()));
               }
            }
            return sb.toString();
        });
        expddls.put("sqlite",(conn,type,args)->{
            return "";
        });
        expdmls.put("oracle",(conn,os,args)->{
            try(OutputStreamWriter fr=new OutputStreamWriter(os,"utf-8");){
                for(String tableName:args){
                    DatabaseMetaData metaData = conn.getMetaData();
                    ResultSet colRes = metaData.getColumns(null, null, tableName, "%");
                    StringBuilder before=new StringBuilder("select 'insert into "+tableName+"(");
                    StringBuilder after=new StringBuilder(") values(");
                    while(colRes.next()){
                        String colName=colRes.getString("COLUMN_NAME");
                        int type = colRes.getInt("DATA_TYPE");
                        before.append(colName);
                        before.append(",");
                        if(type==Types.TIME||type==Types.TIMESTAMP||type==Types.TIMESTAMP_WITH_TIMEZONE||type==Types.TIME_WITH_TIMEZONE||type==Types.DATE){
                            after.append("to_date(''yyyy-mm-dd hh24:mi:ss'','''||to_char('yyyy-mm-dd hh24:mi:ss',"+colName+")||'''),");
                        }else{
                            after.append("'''||"+colName+"||''',");
                        }
                    }
                    after.deleteCharAt(after.length()-1);
                    before.deleteCharAt(after.length()-1);
                    after.append(");' from ");
                    after.append(tableName);
                    String sql=before.toString()+after.toString();
                }

            }catch(Exception e){
                log.error(e.getMessage(),e);
            }


        });

    }
    @Override
    public String prefix() {
        return "db";
    }

}
