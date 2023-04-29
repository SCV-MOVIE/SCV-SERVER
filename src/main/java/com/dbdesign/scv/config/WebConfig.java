package com.dbdesign.scv.config;

import com.dbdesign.scv.util.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor()) // 로그인이 필요한 기능에 대해 로그인 여부 체크
                .order(0) // 낮을 수록 먼저 호출
                .addPathPatterns(); // TODO: api 개발하면서 추가할 예정
    }
}