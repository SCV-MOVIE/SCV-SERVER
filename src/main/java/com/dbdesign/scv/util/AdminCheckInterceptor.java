package com.dbdesign.scv.util;

import com.dbdesign.scv.entity.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminCheckInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false); // 세션 가져옴

        Admin loginMember = (Admin) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) { // 세션에 저장된 회원이 없는 경우

            log.error("접근할 수 없는 페이지입니다.");
            response.sendError(HttpStatus.FORBIDDEN.value(), "접근할 수 없는 페이지입니다.");
            return false;
        }
        return true;
    }
}
