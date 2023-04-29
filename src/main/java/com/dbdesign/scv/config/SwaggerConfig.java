package com.dbdesign.scv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.swaggerInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dbdesign.scv.controller")) // request handler selector 가 스캔할 패키지 선택
                .paths(PathSelectors.any()) // 모든 url 에 대해 명세서 작성
                .build()
                .useDefaultResponseMessages(true);
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder()
                .title("SCV API Documentation")
                .description("SpringBoot로 개발해보는 웹 프로젝트입니다.")
                .version("1.0")
                .build();
    }
}