package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.BankDTO;
import com.dbdesign.scv.dto.HandleTicketDTO;
import com.dbdesign.scv.entity.*;
import com.dbdesign.scv.repository.*;
import com.dbdesign.scv.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BankService {

    private final BankRepository bankRepository;
    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;
    private final TicketRepository ticketRepository;
    private final TicketSeatRepository ticketSeatRepository;

    public BankService(BankRepository bankRepository, PaymentRepository paymentRepository, ClientRepository clientRepository, TicketRepository ticketRepository, TicketSeatRepository ticketSeatRepository) {
        this.bankRepository = bankRepository;
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
        this.ticketRepository = ticketRepository;
        this.ticketSeatRepository = ticketSeatRepository;
    }

    // 계좌 이체 요청에 대한 승인 또는 거절 (뱅크 어드민)
    @Transactional
    public void handleTicket(HandleTicketDTO handleTicketDTO) {

        String requestDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Bank bank = bankRepository.findBankById((long) handleTicketDTO.getBankId());

        if (bank == null) {
            throw new IllegalArgumentException("승인 혹은 거절할 결제 내역이 없습니다.");
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

            // ticket_seat 에서 row 삭제
            ticketSeatRepository.deleteAllByTicket(ticket);

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
            if (!bank.getApproveNm().isEmpty()) {
                bankDTO.setApproveNm(bank.getApproveNm());
            }

            bankDTOList.add(bankDTO);
        }

        return bankDTOList;
    }
}
