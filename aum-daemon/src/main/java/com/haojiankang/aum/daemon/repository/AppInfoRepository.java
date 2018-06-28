package com.haojiankang.aum.daemon.repository;

import com.haojiankang.aum.daemon.model.AppInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public class AppInfoRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private RowMapper<AppInfo> appInfoRowMapper=new BeanPropertyRowMapper(AppInfo.class);
    private ResultSetExtractor<AppInfo> appinfoRse= rs->{
        if(rs.isClosed())
            return null;
        return appInfoRowMapper.mapRow(rs,1);
    };
    @Transactional(readOnly = true)
    public List<AppInfo> findAll() {
        return jdbcTemplate.query("select * from appinfo",appInfoRowMapper) ;
    }
    @Transactional(readOnly = true)
    public AppInfo findById(String id) {
        return jdbcTemplate.query("select * from appinfo where id=?", new Object[]{id},appinfoRse);
    }
    @Transactional(readOnly = true)
    public AppInfo findByAppcodeAndPointcode(String appcode, String pointcode) {
        return jdbcTemplate.query("select * from appinfo where appcode=? and  pointcode=?", new Object[]{appcode,pointcode},appinfoRse);
    }
    public AppInfo create(AppInfo appInfo) {
        String sql = "insert into appinfo(id,appcode,pointcode,properties,version) values(?,?,?,?,?)";
        if(appInfo.getId()==null){
            appInfo.setId(UUID.randomUUID().toString().replace("-",""));
        }
        jdbcTemplate.update(sql,appInfo.getId(),appInfo.getAppCode(),appInfo.getPointCode(),appInfo.getProperties(), appInfo.getVersion());
        return appInfo;
    }
    public void update( AppInfo info) {
        jdbcTemplate.update("update appinfo set appcode=?,pointcode=?,properties=?,version=? where id=?",
                new Object[]{info.getAppCode(), info.getPointCode(),info.getProperties(), info.getVersion(),info.getId()});
    }
    public void delete(String id) {
        jdbcTemplate.update("delete from appinfo where id=?",
                new Object[]{id});
    }

}
