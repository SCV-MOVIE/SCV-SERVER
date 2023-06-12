package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.LoginDTO;
import com.dbdesign.scv.dto.UpdateUserInfoByAdminDTO;
import com.dbdesign.scv.entity.Admin;
import com.dbdesign.scv.entity.Client;
import com.dbdesign.scv.repository.AdminRepository;
import com.dbdesign.scv.repository.ClientRepository;
import com.dbdesign.scv.util.SessionConst;
import com.dbdesign.scv.util.TestConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Service
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;

    public AdminService(AdminRepository adminRepository, ClientRepository clientRepository) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
    }

    // 로그인
    public void login(LoginDTO loginDTO) {

        Admin adminMember = adminRepository.findAdminByLoginId(loginDTO.getLoginId());

        // 아이디 존재하지 않는 경우
        if (adminMember == null) { // 받은 loginId 로 회원이 존재하는 지 확인
            throw new IllegalArgumentException("아이디가 존재하지 않습니다.");
        }

        if (loginDTO.getPassword().equals(TestConst.ADMIN_PWD)) {
            createAdminSession(adminMember);
        } else {
            throw new IllegalArgumentException("아이디 또는 비밀번호를 잘못 입력하셨습니다.");
        }
    }

    // 어드민 유저 세션 생성
    public void createAdminSession(Admin admin) {
        HttpSession session = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, admin);

        log.info("기존의 세션 반환 및 혹은 세션을 생성하였습니다.");
        log.info("해당 세션 : " + session);
    }

    // 로그아웃
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 모든 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }

    // 회원 정보 수정 (어드민)
    @Transactional
    public void updateUserInfo(UpdateUserInfoByAdminDTO updateUserInfoByAdminDTO) {

        Client client = clientRepository.findClientById(updateUserInfoByAdminDTO.getClientId());

        // 고객이 존재하지 않는 경우
        if (client == null) { // 받은 id로 회원이 존재하는 지 확인
            throw new IllegalArgumentException("고객이 존재하지 않습니다.");
        }

        client.setMembership(updateUserInfoByAdminDTO.getNewMembership());
        client.setPoint(updateUserInfoByAdminDTO.getNewPoint());
        clientRepository.save(client);
    }
}
