package com.haojiankang.aum.daemon.service;

import com.haojiankang.aum.daemon.model.AppInfo;
import com.haojiankang.aum.daemon.model.PackageInfo;
import com.haojiankang.aum.daemon.repository.AppInfoRepository;
import com.haojiankang.aum.daemon.repository.PackageInfoRepository;
import com.haojiankang.aum.tools.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PackageInfoService {
    private static final String BASE_DIR=new ApplicationHome(PackageInfoService.class).getDir().getAbsolutePath()+File.separator;
    private static final String PKG_DIR=String.format("%s%s%s",BASE_DIR,"package",File.separator);
    private static final String PKG_TMP_DIR=String.format("%s%s%s",PKG_DIR,File.separator,"tmp");
    private static final String UPGRADE_PROGRAM=String.format("%s%s%s%s",BASE_DIR,"ext",File.separator,"upgrade.jar");
    public static volatile boolean UPDATE_LOCK=false;
    @Autowired
    private PackageInfoRepository pkgRep;
    @Autowired
    private AppInfoRepository appRep;
    @Autowired
    private Environment env;
    public void save(PackageInfo info, byte[] bytes ) throws  Exception{
        List<PackageInfo> list=pkgRep.findByInfo(info,"and appcode=:appcode and pointcode=:pointcode and version=:version");
        if(list==null||list.size()==0||!list.stream().anyMatch(p->p.getState()==null||p.getState()==true)){
            info.setId(null);
            info.setUploadtime(new Date());
            pkgRep.create(info);
            FileUtils.writeFile(bytes,new File(PKG_DIR,info.getAppcode()+File.separator+info.getPointcode()),info.getVersion());
        }
    }
    public void savePkg(PackageInfo info, InputStream ins ) throws  Exception{
        PackageInfo pkg = pkgRep.findById(info.getId());
        if(pkg==null){
            pkgRep.create(info);
        }else{
            info.setUploadtime(new Date());
            pkgRep.update(info);
        }
        FileUtils.writeFile(ins,new File(PKG_DIR,info.getAppcode()+File.separator+info.getPointcode()),info.getVersion());
    }
    public void runUpdate(String appcode,String pointcode){
        AppInfo appinfo= appRep.findByAppcodeAndPointcode(appcode,pointcode);
        List<PackageInfo> pkgList = getPackageList(appcode, pointcode, appinfo);
        try {
            call(appinfo, pkgList);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public List<PackageInfo> getPackageList(String appcode, String pointcode, AppInfo appinfo) {
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

    private void call(AppInfo appinfo,List<PackageInfo> pkgList) throws IOException{
        File argFile = lockFile(appinfo, pkgList);
        File execFile = ready();
        //java -jar 调用 tmp下的jar
        try{
            String cmd=String.format(env.getProperty("aum.daemon.exec"),execFile.getAbsolutePath(),argFile.getAbsolutePath(),BASE_DIR);
            log.debug("run cmd:{}",cmd);
            if(!UPDATE_LOCK){
                UPDATE_LOCK=true;
                Runtime.getRuntime().exec(cmd);
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private File ready() throws IOException{
        File upgrade=new File(UPGRADE_PROGRAM);
        File execFile=new File(PKG_TMP_DIR,upgrade.getName());
        execFile.delete();
        FileUtils.copyFile(upgrade,execFile);
        return execFile;
    }

    private File lockFile(AppInfo appinfo, List<PackageInfo> pkgList) throws IOException {
        StringBuilder args=new StringBuilder();
        args.append(String.format("code:%s,point:%s,basedir:%s,properties:%s,version:",appinfo.getAppCode(),appinfo.getPointCode(),BASE_DIR,appinfo.getProperties()));
        pkgList.stream().forEach(pkg->{
            args.append(String.format("%s,",pkg.getVersion()));
        });
        File argFile=new File(new ApplicationHome(PackageInfoService.class).getDir(),UUID.randomUUID().toString()+".uuid");
        FileUtils.writeFile(args.toString(),argFile);
        return argFile;
    }
    public List<PackageInfo> listAll(){
        return pkgRep.findAll();
    }
}
