package com.haojiankang.aum.daemon.repository;

import com.haojiankang.aum.daemon.model.DaemonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public class DaemonInfoRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedTemplate;
    private RowMapper<DaemonInfo> daemonInfoRowMapper=new BeanPropertyRowMapper(DaemonInfo.class);
    private ResultSetExtractor<DaemonInfo> appinfoRse= rs->{
        if(rs.isClosed())
            return null;
        return daemonInfoRowMapper.mapRow(rs,1);
    };
    @Transactional(readOnly = true)
    public List<DaemonInfo> findAll() {
        return namedTemplate.query("select * from daemonInfo",daemonInfoRowMapper) ;
    }
    @Transactional(readOnly = true)
    public List<DaemonInfo> findByInfo(DaemonInfo info,String condition) {
        return namedTemplate.query("select * from daemonInfo where 1=1  "+condition,new BeanPropertySqlParameterSource(info),daemonInfoRowMapper);
    }
    public DaemonInfo create(DaemonInfo info) {
        String sql = "insert into DaemonInfo(version,time) values(:version,:time)";
        namedTemplate.update(sql,new BeanPropertySqlParameterSource(info));
        return info;
    }
    public void update( DaemonInfo info) {
        namedTemplate.update("update DaemonInfo set version=:version,time=:time",new BeanPropertySqlParameterSource(info));
    }
    public void delete() {
        namedTemplate.update("delete from DaemonInfo",new HashMap<>());
    }


}
