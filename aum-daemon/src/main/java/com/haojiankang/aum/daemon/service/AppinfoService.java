package com.haojiankang.aum.daemon.service;

import com.haojiankang.aum.daemon.model.AppInfo;
import com.haojiankang.aum.daemon.model.SSTO;
import com.haojiankang.aum.daemon.repository.AppInfoRepository;
import com.haojiankang.aum.tools.HttpUtils;
import com.haojiankang.aum.tools.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class AppinfoService {

    @Autowired
    Environment environment;
    @Autowired
    private AppInfoRepository repository;
    public void register(AppInfo info){
        String host=environment.getProperty("vcc.host");
        String appregister=environment.getProperty("vcc.url.appregister");
        AppInfo queryInfo=repository.findByAppcodeAndPointcode(info.getAppCode(),info.getPointCode());
        if(queryInfo!=null&&!info.getProperties().equals(queryInfo.getProperties())){
            info.setId(queryInfo.getId());
            repository.update(info);
        }else if(queryInfo==null){
            info.setId(null);
            repository.create(info);
        }
        info.setStatus(repository.findById(info.getId()).getStatus());
        try{
            String post = HttpUtils.post(host + appregister, HttpUtils.formBody("pointCode", info.getPointCode(), "appCode", info.getAppCode(), "version", info.getVersion(), "url", environment.getProperty("vcc.callback")), null);
            SSTO ssto = JsonUtils.parse(post, SSTO.class);
            if(ssto!=null){
                log.debug("register response:{}",ssto);
            }
        }catch(IOException  e){
            log.error(e.getMessage(),e);
        }
    }
    public List<AppInfo> listAll(){
        return repository.findAll();
    }
}
