package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.*;
import com.dbdesign.scv.entity.Client;
import com.dbdesign.scv.entity.Ticket;
import com.dbdesign.scv.repository.ClientRepository;
import com.dbdesign.scv.repository.TicketRepository;
import com.dbdesign.scv.util.SessionConst;
import com.dbdesign.scv.util.TestConst;
import com.dbdesign.scv.util.UserLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public void registerUser(RegisterFormDTO registerFormDTO) {

        // 아이디 중복 체크
        if (checkLoginIdDuplicate(registerFormDTO.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 전화번호, 주민번호 중복 체크
        for (Client client : clientRepository.findAll()) {
            if (client.getPhoneNm().equals(registerFormDTO.getPhoneNm()) || passwordEncoder.matches(registerFormDTO.getSecurityNm(), client.getSecurityNm())) {
                throw new IllegalArgumentException("이미 회원가입된 회원입니다.");
            }
        }

        Client client = Client.builder()
                .loginId(registerFormDTO.getLoginId())
                .password(passwordEncoder.encode(registerFormDTO.getPassword()))
                .point(0)
                .securityNm(passwordEncoder.encode(registerFormDTO.getSecurityNm()))
                .name(registerFormDTO.getName())
                .phoneNm(registerFormDTO.getPhoneNm())
                .isMember('Y')
                .membership(UserLevel.COMMON)
                .build();

        clientRepository.save(client);
    }

    // 로그인 아이디 중복 체크
    public boolean checkLoginIdDuplicate(String loginId) {

        return clientRepository.existsByLoginId(loginId);
    }

    // 로그인
    public void login(LoginDTO loginDTO) {

        Client clientMember = clientRepository.findClientByLoginId(loginDTO.getLoginId());

        // 아이디 존재하지 않는 경우
        if (clientMember == null) { // 받은 loginId 로 회원이 존재하는 지 확인
            throw new IllegalArgumentException("아이디가 존재하지 않습니다.");
        }

        // 테스트 계정
        if (loginDTO.getPassword().equals(TestConst.ADMIN_PWD)) {

            createClientSession(clientMember);

        } else { // 일반 유저인 경우
            if (passwordEncoder.matches(loginDTO.getPassword(), clientMember.getPassword())) {

                createClientSession(clientMember);
            } else {
                throw new IllegalArgumentException("아이디 또는 비밀번호를 잘못 입력하셨습니다.");
            }
        }
    }

    // 유저 정보 수정
    @Transactional
    public void updateUserInfo(UpdateUserInfoDTO updateUserInfoDTO) {

        Client client = clientRepository.findClientById(updateUserInfoDTO.getId());

        // 수정할 회원이 존재하지 않는 경우
        if (client == null) { // 받은 id 로 회원이 존재하는 지 확인
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        for (Client client1 : clientRepository.findAll()) {
            if (client1 != client) {
                // 본인을 제외하고 새로 수정할 로그인 아이디가 중복되는 경우
                if (client1.getLoginId().equals(updateUserInfoDTO.getNewLoginId())) {
                    throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
                }

                // 본인을 제외하고 새로 수정할 전화번호가 중복되는 경우
                if (client1.getPhoneNm().equals(updateUserInfoDTO.getNewPhoneNm())) {
                    throw new IllegalArgumentException("이미 사용 중인 전화번호입니다.");
                }
            }
        }

        client.setLoginId(updateUserInfoDTO.getNewLoginId());
        client.setName(updateUserInfoDTO.getNewName());
        client.setPhoneNm(updateUserInfoDTO.getNewPhoneNm());

        clientRepository.save(client);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(DeleteClientDTO deleteClientDTO) { // TODO: 상영일정, 티켓 관련 api 작성 이후 테스트해볼 것

        Client client = clientRepository.findClientById(deleteClientDTO.getId());

        // 탈퇴할 회원이 존재하지 않는 경우
        if (client == null) { // 받은 id 로 회원이 존재하는 지 확인
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        // 비밀번호 틀릴 시
        if (!passwordEncoder.matches(deleteClientDTO.getPassword(), client.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // ticket 엔티티의 client_id null 처리
        for (Ticket ticket : ticketRepository.findAllByClientId(client.getId())) {
            ticket.setClient(null);
        }

        // client 물리적 삭제
        clientRepository.delete(client);
    }

    // 일반 유저 세션 생성
    public void createClientSession(Client client) {
        HttpSession session = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, client);

        log.info("기존의 세션 반환 및 혹은 세션을 생성하였습니다.");
        log.info("해당 세션 : " + session);
    }


    // 모든 유저 리스트 반환
    public List<ClientDTO> getUserList() {

        return clientRepository.findAll().stream().map(ClientDTO::from).collect(Collectors.toList());
    }

    // 현재 회원 정보 반환
    public ClientDTO getUserInfo(Client loginMember) {

        if (loginMember == null) {
            throw new IllegalStateException("로그인된 유저가 없습니다.");
        }

        return ClientDTO.from(loginMember);
    }

    // 현재 로그인된 멤버가 있는 여부 반환
    public boolean isMemberLogin(Client loginMember) {

        if (loginMember == null) {
            return false;
        }

        return true;
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
}
