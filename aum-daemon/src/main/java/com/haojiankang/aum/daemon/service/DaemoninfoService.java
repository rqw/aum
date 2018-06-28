package com.haojiankang.aum.daemon.service;

import com.haojiankang.aum.daemon.model.DaemonInfo;
import com.haojiankang.aum.daemon.repository.DaemonInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DaemoninfoService {
    @Autowired
    private DaemonInfoRepository repository;
    public DaemonInfo current(){
        return repository.findAll().get(0);
    }
    public void update(String version){
        DaemonInfo info=new DaemonInfo();
        info.setVersion(version);
        info.setTime(new Date(0));
        repository.update(info);
    }
}
