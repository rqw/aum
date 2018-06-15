package com.haojiankang.aum.daemon.conf;

import com.haojiankang.aum.daemon.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@Slf4j
public class RepositoryConf {
    @Autowired
    private Environment env;
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(env.getProperty("spring.datasource.classname"));
        dataSourceBuilder.url(String.format(env.getProperty("spring.datasource.url"),FileUtils.getBasePath().getAbsolutePath(), File.separator));
        dataSourceBuilder.type(SQLiteDataSource.class);
        return dataSourceBuilder.build();

    }

}
