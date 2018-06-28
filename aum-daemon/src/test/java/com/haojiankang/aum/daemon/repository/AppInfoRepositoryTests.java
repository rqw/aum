package com.haojiankang.aum.daemon.repository;

import com.haojiankang.aum.daemon.model.AppInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AppInfoRepositoryTests{
    @Autowired
    private AppInfoRepository appInfoRepository;

    @Test
    public void testAll(){
        findAll();
        create();
    }

    @Test
    public void findAll()  {
        List<AppInfo> infos = appInfoRepository.findAll();
        assertNotNull(infos);
        assertTrue(!infos.isEmpty());

    }

    @Test
    public void findyId()  {
        AppInfo info = appInfoRepository.findById("1");
        assertNotNull(info);
    }
    private void updateById(String id)  {
        AppInfo info = new AppInfo();
        info.setId(id);
        info.setAppCode("changecode");
        info.setPointCode("changepoint");
        info.setProperties("changePorperties");
        appInfoRepository.update(info);
        AppInfo info2 = appInfoRepository.findById(info.getId());
        assertEquals(info.getAppCode(), info2.getAppCode());
        assertEquals(info.getPointCode(), info2.getPointCode());
        assertEquals(info.getProperties(), info2.getProperties());
    }
    @Test
    public void create() {
        AppInfo info = new AppInfo();
        info.setAppCode("appcode");
        info.setPointCode("pointcode");
        info.setProperties("porperties");
        AppInfo saveInfo = appInfoRepository.create(info);
        log.debug("{}",saveInfo);
        AppInfo queryInfo = appInfoRepository.findById(saveInfo.getId());
        assertEquals(queryInfo.getAppCode(), "appcode");
        assertEquals(queryInfo.getPointCode(), "pointcode");
        assertEquals(queryInfo.getProperties(), "porperties");
        updateById(queryInfo.getId());
        appInfoRepository.delete(queryInfo.getId());
    }
    @Test
    public void findByCode(){
        AppInfo info=appInfoRepository.findByAppcodeAndPointcode("changecode1","changepoint");
        log.debug("{}",info);
    }
}
