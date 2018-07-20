package com.haojiankang.aum.daemon.task;

import com.haojiankang.aum.daemon.model.AppInfo;
import com.haojiankang.aum.daemon.model.SSTO;
import com.haojiankang.aum.daemon.service.AppinfoService;
import com.haojiankang.aum.tools.HttpUtils;
import com.haojiankang.aum.tools.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class AumDaemonTask {

    @Autowired
    Environment environment;
    @Autowired
    AppinfoService appinfoService;
    //@Scheduled(fixedRate = 3*60*60*1000)
    @Scheduled(fixedRate = 10*1000)
    @Async
    public void checkForUpdates(){
        //检查更新定时任务
        String host=environment.getProperty("vcc.host");
        String checkfoudpate=environment.getProperty("vcc.url.checkforupdate");
        List<AppInfo> appInfos = appinfoService.listAll();
        appInfos.forEach(info->{
            try {
                String post = HttpUtils.post(host + checkfoudpate, HttpUtils.formBody("appCode", info.getAppCode(), "pointCode", info.getPointCode(), "version", info.getVersion()), null);
                SSTO ssto = JsonUtils.parse(post, SSTO.class);
                if(ssto!=null&&ssto.getData()!=null){
                    log.debug("check for update request:{}",info);
                    log.debug("check for updates response:{}",ssto.getData());
                }
            }catch(IOException e){
                log.debug("check for update reqerror:{}",info);
                log.error(e.getMessage(),e);
            }
        });
    }
}
