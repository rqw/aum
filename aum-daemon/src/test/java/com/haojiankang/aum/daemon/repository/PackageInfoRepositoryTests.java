package com.haojiankang.aum.daemon.repository;

import com.haojiankang.aum.daemon.po.PackageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class PackageInfoRepositoryTests {
    @Autowired
    private PackageInfoRepository packageInfoRepository;

    @Test
    public void testAll(){
        findAll();
        create();
    }

    @Test
    public void findAll()  {
        List<PackageInfo> infos = packageInfoRepository.findAll();
        assertNotNull(infos);
        assertTrue(!infos.isEmpty());

    }

    @Test
    public void findyId()  {
        PackageInfo info = packageInfoRepository.findById("1");
        assertNotNull(info);
    }
    private void updateById(String id)  {
        PackageInfo info = new PackageInfo();
        info.setId(id);
        info.setAppcode("changecode");
        info.setPointcode("changepoint");
        info.setDependent("changedependent");
        info.setDescribe("changedescribe");
        info.setState(null);
        info.setUpgradetime(new Date());
        info.setUploadtime(new Date());
        info.setVersion("changeversion");
        packageInfoRepository.update(info);
        PackageInfo info2 = packageInfoRepository.findById(info.getId());
        assertTrue(info.equals(info2));
    }
    @Test
    public void create() {
        PackageInfo info = new PackageInfo();
        info.setAppcode("appcode");
        info.setPointcode("pointcode");
        info.setDependent("dependent");
        info.setDescribe("describe");
        info.setState(null);
        info.setUpgradetime(new Date());
        info.setUploadtime(new Date());
        info.setVersion("version");
        PackageInfo saveInfo = packageInfoRepository.create(info);
        log.debug("{}",saveInfo);
        PackageInfo queryInfo = packageInfoRepository.findById(saveInfo.getId());
        assertTrue(info.equals(queryInfo));
        updateById(queryInfo.getId());
        packageInfoRepository.delete(queryInfo.getId());
    }

}
