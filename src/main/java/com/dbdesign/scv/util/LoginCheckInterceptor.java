package com.dbdesign.scv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false); // 세션 가져옴

        if (session == null) { // 세션이 비어있다면, 500 에러를 뱉는다

            log.error("로그인 후, 사용해주세요.");
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "로그인 후, 사용해주세요.");
            return false;
        }
        return true;
    }
}
