package com.haojiankang.aum.daemon.service;

import com.haojiankang.aum.daemon.po.AppInfo;
import com.haojiankang.aum.daemon.po.PackageInfo;
import com.haojiankang.aum.daemon.repository.AppInfoRepository;
import com.haojiankang.aum.daemon.repository.PackageInfoRepository;
import com.haojiankang.aum.daemon.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PackageInfoService {
    private static final String BASE_DIR=FileUtils.getBasePath().getAbsolutePath()+File.separator;
    private static final String PKG_DIR=String.format("%s%s%s",BASE_DIR,"package",File.separator);
    private static final String PKG_TMP_DIR=String.format("%s%s%s",PKG_DIR,File.separator,"tmp");
    private static final String UPGRADE_PROGRAM=String.format("%s%s%s%s",BASE_DIR,"ext",File.separator,"upgrade.jar");
    private static final String UPGRADE_CMD="cmd /c start java -jar %s update %s";
    @Autowired
    private PackageInfoRepository pkgRep;
    @Autowired
    private AppInfoRepository appRep;
    public void save(PackageInfo info, byte[] bytes ) throws  Exception{
        List<PackageInfo> list=pkgRep.findByInfo(info,"and appcode=:appcode and pointcode=:pointcode and version=:version");
        if(list==null||list.size()==0||!list.stream().anyMatch(p->p.getState()==null||p.getState()==true)){
            info.setId(null);
            info.setUploadtime(new Date());
            pkgRep.create(info);
            FileUtils.writeFile(bytes,new File(PKG_DIR,info.getAppcode()+File.separator+info.getPointcode()),info.getVersion());
        }
    }

    public void runUpdate(String appcode,String pointcode){
        
        AppInfo appinfo= appRep.findByAppcodeAndPointcode(appcode,pointcode);
        List<PackageInfo> pkgList = getPackageList(appcode, pointcode, appinfo);
        call(appinfo,pkgList);
    }

    private List<PackageInfo> getPackageList(String appcode, String pointcode, AppInfo appinfo) {
        PackageInfo query=new PackageInfo();
        query.setAppcode(appcode);
        query.setPointcode(pointcode);
        List<PackageInfo> list=pkgRep.findByInfo(query,"and appcode=:appcode and pointcode=:pointcode order by uploadtime");
        List<PackageInfo> pkgList=new ArrayList<>();
        list.stream().forEach(pkg->{
            if(appinfo.getVersion().equals(pkg.getDependent())){
                appinfo.setVersion(pkg.getVersion());
                pkgList.add(pkg);
            }
        });
        return pkgList;
    }

    private void call(AppInfo appinfo,List<PackageInfo> pkgList){
        File argFile = lockFile(appinfo, pkgList);
        File execFile = ready();
        //java -jar 调用 tmp下的jar
        try{
            String cmd=String.format(UPGRADE_CMD,execFile.getAbsolutePath(),argFile.getAbsolutePath());
            log.debug("run cmd:{}",cmd);
            Runtime.getRuntime().exec(cmd);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private File ready() {
        File upgrade=new File(UPGRADE_PROGRAM);
        File execFile=new File(PKG_TMP_DIR,upgrade.getName());
        execFile.delete();
        FileUtils.copyFile(upgrade,execFile);
        return execFile;
    }

    private File lockFile(AppInfo appinfo, List<PackageInfo> pkgList) {
        StringBuilder args=new StringBuilder();
        args.append(String.format("code:%s,point:%s,basedir:%s,properties:%s,version:",appinfo.getAppCode(),appinfo.getPointCode(),BASE_DIR,appinfo.getProperties()));
        pkgList.stream().forEach(pkg->{
            args.append(String.format("%s,",pkg.getVersion()));
        });
        File argFile=new File(FileUtils.getBasePath(),UUID.randomUUID().toString()+".uuid");
        FileUtils.writeFile(args.toString(),argFile);
        return argFile;
    }

}
