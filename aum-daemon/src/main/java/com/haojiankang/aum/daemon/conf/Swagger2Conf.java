package com.haojiankang.aum.daemon.conf;

import com.haojiankang.aum.daemon.AumDaemonApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class Swagger2Conf {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //页面标题
                        .title("更新服务API")
                        //版本号
                        .version("1.0")
                        //描述
                        .description("更新服务相关接口api信息。")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(AumDaemonApplication.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build();
    }
}
