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
    private static final String PKG_DIR="package"+File.separator+"%s"+File.separator+"%s";
    private static final String PKG_TMP_DIR=FileUtils.getBasePath().getAbsolutePath()+File.separator+"package"+File.separator+"tmp";
    private static final String UPGRADE_PROGRAM=FileUtils.getBasePath().getAbsolutePath()+File.separator+"ext"+File.separator+"upgrade.jar";
    private static final String UPGRADE_CMD="cmd /c start java -jar %s %s";
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
            FileUtils.writeFile(bytes,getPackageDir(info.getAppcode(),info.getPointcode() ),info.getVersion());
        }
    }
    private File getPackageDir(String appcode,String pointcode){
       return new File(FileUtils.getBasePath(), String.format(PKG_DIR,appcode,pointcode ));
    }
    public void runUpdate(String appcode,String pointcode){
        File pkgDir=getPackageDir(appcode,pointcode);
        PackageInfo query=new PackageInfo();
        query.setAppcode(appcode);
        query.setPointcode(pointcode);
        AppInfo appinfo= appRep.findByAppcodeAndPointcode(appcode,pointcode);
        List<PackageInfo> list=pkgRep.findByInfo(query,"and appcode=:appcode and pointcode=:pointcode order by uploadtime");
        List<PackageInfo> pkgList=new ArrayList<>();
        list.stream().forEach(pkg->{
            if(appinfo.getVersion().equals(pkg.getDependent())){
                appinfo.setVersion(pkg.getVersion());
                pkgList.add(pkg);
            }
        });
        updateProgram(appinfo,pkgList);
    }
    private void updateProgram(AppInfo appinfo,List<PackageInfo> pkgList){

        StringBuilder args=new StringBuilder();
        args.append(String.format("code:%s,point:%s,properties:%s\r\nversion:",appinfo.getAppCode(),appinfo.getPointCode(),appinfo.getProperties()));
        pkgList.stream().forEach(pkg->{
            args.append(String.format("%s,",pkg.getVersion()));
        });
        File argFile=new File(UUID.randomUUID().toString());
        FileUtils.writeFile(args.toString(),argFile);

        File upgrade=new File(UPGRADE_PROGRAM);
        //删除tmp 下的 upgrade.jar
        File execFile=new File(PKG_TMP_DIR,upgrade.getName());
        execFile.delete();
        //把upgrade.jar 复制到 tmp下
        FileUtils.copyFile(upgrade,execFile);
        //java -jar 调用 tmp下的jar
        try{
            Runtime.getRuntime().exec(String.format(UPGRADE_CMD,execFile.getAbsolutePath(),argFile.getAbsolutePath()));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

}
