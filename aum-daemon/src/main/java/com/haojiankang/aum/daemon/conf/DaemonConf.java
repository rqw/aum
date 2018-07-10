package com.haojiankang.aum.daemon.conf;

import com.haojiankang.aum.api.AumApi;
import com.haojiankang.aum.api.utils.JsonUtils;
import com.haojiankang.aum.daemon.service.DaemoninfoService;
import com.haojiankang.aum.tools.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class DaemonConf {
    @Autowired
    private Environment environment;

    @Autowired
    private DaemoninfoService service;
    @Bean
    CommandLineRunner registerRunner() {
        return args -> {
            String appcode = environment.getProperty("aum.daemon.appcode");
            String pointcode = environment.getProperty("aum.daemon.pointcode");
            String startup = environment.getProperty("aum.daemon.startup");
            String shutdown = environment.getProperty("aum.daemon.shutdown");
            AumApi api = AumApi.api();
            api.setAppCode(appcode);
            api.setPointCode(pointcode);
            api.setVersion(service.current().getVersion());
            api.getProperties().put("app.root", new ApplicationHome(DaemonConf.class).getDir().getAbsolutePath());
            api.getProperties().put("startup", startup);
            api.getProperties().put("shutdown", shutdown);
            Map<String,String> master=new HashMap<>();
            master.put("driverclass",environment.getProperty("spring.datasource.classname")) ;
            master.put("dbtype","sqlite") ;
            master.put("url",String.format(environment.getProperty("spring.datasource.url"),new ApplicationHome(DaemonConf.class).getDir().getAbsolutePath(), File.separator)) ;
            api.getProperties().put("master",JsonUtils.stringify(master));
            api.register();
        };
    }
}
