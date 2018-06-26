package com.haojiankang.aum.daemon.repository;

import com.haojiankang.aum.daemon.po.PackageInfo;
import com.haojiankang.aum.daemon.utils.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public class PackageInfoRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedTemplate;
    private RowMapper<PackageInfo> packageInfoRowMapper=new BeanPropertyRowMapper(PackageInfo.class);
    private ResultSetExtractor<PackageInfo> appinfoRse= rs->{
        if(rs.isClosed())
            return null;
        return packageInfoRowMapper.mapRow(rs,1);
    };
    @Transactional(readOnly = true)
    public List<PackageInfo> findAll() {
        return namedTemplate.query("select * from packageinfo",packageInfoRowMapper) ;
    }
    @Transactional(readOnly = true)
    public PackageInfo findById(String id) {
        return namedTemplate.query("select * from packageinfo where id=:id", Maps.newHMap("id",id),appinfoRse);
    }
    @Transactional(readOnly = true)
    public List<PackageInfo> findByInfo(PackageInfo info,String condition) {
        return namedTemplate.query("select * from packageinfo where 1=1  "+condition,new BeanPropertySqlParameterSource(info),packageInfoRowMapper);
    }
    public PackageInfo create(PackageInfo info) {
        String sql = "insert into packageinfo(id,version,dependent,describe,appcode,pointcode,uploadtime,upgradetime,state) values(:id,:version,:dependent,:describe,:appcode,:pointcode,:uploadtime,:upgradetime,:state)";
        if(info.getId()==null){
            info.setId(UUID.randomUUID().toString().replace("-",""));
        }
        namedTemplate.update(sql,new BeanPropertySqlParameterSource(info));
        return info;
    }
    public void update( PackageInfo info) {
        namedTemplate.update("update packageinfo set version=:version,dependent=:dependent,describe=:describe,appcode=:appcode,pointcode=:pointcode,uploadtime=:uploadtime,upgradetime=:upgradetime,state=:state where id=:id",
                new BeanPropertySqlParameterSource(info));
    }
    public void delete(String id) {
        namedTemplate.update("delete from packageinfo where id=:id",Maps.newHMap("id",id));
    }


}
