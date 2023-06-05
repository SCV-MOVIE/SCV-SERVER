package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.BankDTO;
import com.dbdesign.scv.dto.HandleTicketDTO;
import com.dbdesign.scv.dto.LoginDTO;
import com.dbdesign.scv.entity.*;
import com.dbdesign.scv.repository.*;
import com.dbdesign.scv.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class BankAdminService {

    private final BankRepository bankRepository;
    private final BankAdminRepository bankAdminRepository;
    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;
    private final TicketRepository ticketRepository;

    public BankAdminService(BankRepository bankRepository, BankAdminRepository bankAdminRepository, PaymentRepository paymentRepository, ClientRepository clientRepository, TicketRepository ticketRepository) {
        this.bankRepository = bankRepository;
        this.bankAdminRepository = bankAdminRepository;
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
        this.ticketRepository = ticketRepository;
    }

    // 로그인
    public void login(LoginDTO loginDTO) {

        BankAdmin bankAdminMember = bankAdminRepository.findBankAdminByLoginId(loginDTO.getLoginId());

        // 아이디 존재하지 않는 경우
        if (bankAdminMember == null) { // 받은 loginId 로 회원이 존재하는 지 확인
            throw new IllegalArgumentException("아이디가 존재하지 않습니다.");
        }

        if (loginDTO.getPassword().equals(TestConst.BANK_ADMIN_PWD)) {
            createBankAdminSession(bankAdminMember);
        } else {
            throw new IllegalArgumentException("아이디 또는 비밀번호를 잘못 입력하셨습니다.");
        }
    }

    // 뱅크어드민 유저 세션 생성
    public void createBankAdminSession(BankAdmin bankAdmin) {
        HttpSession session = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, bankAdmin);

        log.info("기존의 세션 반환 및 혹은 세션을 생성하였습니다.");
        log.info("해당 세션 : " + session);
    }

    // 계좌 이체 요청에 대한 승인 또는 거절 (뱅크 어드민)
    @Transactional
    public void handleTicket(HandleTicketDTO handleTicketDTO) {

        String requestDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Bank bank = bankRepository.findBankById((long) handleTicketDTO.getBankId());

        if (bank == null) {
            throw new IllegalArgumentException("승인 혹은 거절할 결제 내역이 없습니다.");
        }

        if (!bank.getStatus().equals(BankStatus.STANDBY)) {
            throw new IllegalArgumentException("승인 혹은 거절할 수 없는 결제 내역입니다.");
        }

        if (handleTicketDTO.getStatus().equals(BankStatus.STANDBY)) {
            throw new IllegalArgumentException("이미 승인 대기 중인 결제 내역입니다.");
        }

        // 승인(계좌이체) -> 계좌이체의 경우, 은행으로부터 승인 번호를 발급받아야 하므로 승인번호 생성
        if (handleTicketDTO.getStatus().equals(BankStatus.APPROVED) && bank.getMethod().equals(BankMethod.ACCOUNT)) {

            // bank 의 approveNm 저장 및 status 를 승인 상태로 변경
            String approveNm = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            bank.setApproveNm(approveNm);
            bank.setStatus(BankStatus.APPROVED);
            bank.setUpdatedAt(requestDateTime);
            bankRepository.save(bank);

            // payment approveNm 전달
            Payment payment = paymentRepository.findPaymentByBankId(bank.getId().intValue());

            if (payment == null) {
                throw new IllegalArgumentException("승인할 결제 내역(Payment)이 없습니다.");
            }

            payment.setApproveNm(approveNm);
            paymentRepository.save(payment);

            // ticket 상태를 PAYED 로 저장
            Ticket ticket = payment.getTicket();
            ticket.setStatus(TicketStatus.PAYED);
            ticket.setUpdatedAt(requestDateTime);
            ticketRepository.save(ticket);
        }

        // 거절
        if (handleTicketDTO.getStatus().equals(BankStatus.REJECTED)) {

            // bank status 를 거절 상태로 변경
            bank.setStatus(BankStatus.REJECTED);
            bank.setUpdatedAt(requestDateTime);
            bankRepository.save(bank);

            // payment 조회
            Payment payment = paymentRepository.findPaymentByBankId(bank.getId().intValue());

            if (payment == null) {
                throw new IllegalArgumentException("거절할 결제 내역(Payment)이 없습니다.");
            }

            // ticket 상태를 REJECTED 로 저장
            Ticket ticket = payment.getTicket();
            ticket.setStatus(TicketStatus.REJECTED);
            ticket.setUpdatedAt(requestDateTime);
            ticketRepository.save(ticket);

            // 포인트로 환급
            Client client = payment.getTicket().getClient();

            int refundedPoint = 0;

            // 거절한 결제 방식이 ACCOUNT 인 경우, 사용한 point 만 환급
            if (payment.getMethod().equals(PaymentMethod.ACCOUNT)) {
                refundedPoint = payment.getTicket().getUsedPoint();
            } else { // 거절한 결제 방식이 CARD
                refundedPoint = payment.getTicket().getPrice();
            }

            client.setPoint(client.getPoint() + refundedPoint);
            clientRepository.save(client);
        }
    }

    // 모든 결제 내역 조회 (뱅크 어드민)
    public List<BankDTO> showBanks() {

        List<BankDTO> bankDTOList = new ArrayList<>();
        for (Bank bank : bankRepository.findAll()) {

            // 서비스 단에서 넣을 것 주입
            BankDTO bankDTO = BankDTO.from(bank);
            if (bank.getApproveNm() != null) {
                bankDTO.setApproveNm(bank.getApproveNm());
            }

            bankDTOList.add(bankDTO);
        }

        return bankDTOList;
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
