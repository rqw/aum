package com.haojiankang.aum.daemon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class AumDaemonApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AumDaemonApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(AumDaemonApplication.class, args);
    }
}
