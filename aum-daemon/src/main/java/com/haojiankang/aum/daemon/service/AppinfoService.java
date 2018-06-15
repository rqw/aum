package com.haojiankang.aum.daemon.service;

import com.haojiankang.aum.daemon.po.AppInfo;
import com.haojiankang.aum.daemon.repository.AppInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppinfoService {
    @Autowired
    private AppInfoRepository repository;
    public void register(AppInfo info){
        AppInfo queryInfo=repository.findByAppcodeAndPointcode(info.getAppCode(),info.getPointCode());
        if(queryInfo!=null&&!info.getProperties().equals(queryInfo.getProperties())){
            info.setId(queryInfo.getId());
            repository.update(info);
        }else if(queryInfo==null){
            info.setId(null);
            repository.create(info);
        }
    }
}
