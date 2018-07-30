package com.haojiankang.aum.daemon.task;

import com.haojiankang.aum.daemon.model.AppInfo;
import com.haojiankang.aum.daemon.model.PackageInfo;
import com.haojiankang.aum.daemon.model.SSTO;
import com.haojiankang.aum.daemon.service.AppinfoService;
import com.haojiankang.aum.daemon.service.PackageInfoService;
import com.haojiankang.aum.tools.HttpUtils;
import com.haojiankang.aum.tools.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AumDaemonTask {

    @Autowired
    Environment environment;
    @Autowired
    AppinfoService appinfoService;
    @Autowired
    PackageInfoService pkgService;

    //    @Scheduled(fixedRate = 10 * 1000)
    @Scheduled(fixedRate = 3*60*60*1000)
    @Async
    public void checkForUpdates() {
        //正在执行更新
        if (!pkgService.UPDATE_LOCK) {
            //检查更新定时任务
            String host = environment.getProperty("vcc.host");
            String checkfoudpate = environment.getProperty("vcc.url.checkforupdate");
            String downfile = environment.getProperty("vcc.url.downfile");
            List<AppInfo> appInfos = appinfoService.listAll();
            appInfos.forEach(info -> {
                try {
                    //先检查当前是否还有没有升级的安装包，如果存在就先查询出来执行完升级后再检查更新
                    List<PackageInfo> packageList = pkgService.getPackageList(info.getAppCode(), info.getPointCode(), info);
                    if (packageList != null&&packageList.size()>0) {
                        pkgService.runUpdate(info.getAppCode(),info.getPointCode());
                    } else {
                        checkAndDownload(host, checkfoudpate, downfile, info);
                    }
                } catch (IOException e) {
                    log.debug("check for update reqerror:{}", info);
                    log.error(e.getMessage(), e);
                }
            });
        }

    }

    private void checkAndDownload(String host, String checkfoudpate, String downfile, AppInfo info) throws IOException {
        String post = HttpUtils.post(host + checkfoudpate, HttpUtils.formBody("appCode", info.getAppCode(), "pointCode", info.getPointCode(), "version", info.getVersion()), null);
        SSTO ssto = JsonUtils.parse(post, SSTO.class);
        if (ssto != null && ssto.getData() != null) {
            log.debug("check for update request:{}", info);
            log.debug("check for updates response:{}", ssto.getData());
            List list = (List) ssto.getData();
            list.forEach(item -> {
                Map<String, String> map = (Map<String, String>) item;
                PackageInfo pkg = new PackageInfo();
                pkg.setAppcode(map.get("appCode"));
                pkg.setDependent(map.get("dependent"));
                pkg.setId(map.get("id"));
                pkg.setPointcode(info.getPointCode());
                pkg.setVersion(map.get("version"));
                String fileId = map.get("fileId");
                try {
                    pkg.setUploadtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get("createTime")));
                    HttpUtils.requestResponse(host + downfile.replace("{id}", fileId), null, null, null,response->{
                            try {
                                pkgService.savePkg(pkg,response.getEntity().getContent());
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            });

        }
    }

    public static void main(String[] args) throws Exception {
        String post = HttpUtils.post("http://vcc.haojiankang.com/information/listAll", HttpUtils.formBody("appCode", "hid", "pointCode", "hs", "version", "3.0.5"), null);
        SSTO ssto = JsonUtils.parse(post, SSTO.class);
        System.out.println(ssto);
    }
}
