package com.hair.management.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                // 是否开启
                .enable(true).select()
                // 扫描的路径包
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("俊杰发艺")
                .description("springboot | swagger")
                // 作者信息
                .contact(new Contact("fairManagement", "www.baidu.com", "1107746668@qq.com"))
                .version("1.0.0")
                .build();
    }
}
