package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.SeatDTO;
import com.dbdesign.scv.dto.TicketCheckFormDTO;
import com.dbdesign.scv.dto.TicketDTO;
import com.dbdesign.scv.dto.TicketReserveFormDTO;
import com.dbdesign.scv.entity.*;
import com.dbdesign.scv.repository.*;
import com.dbdesign.scv.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private final ClientRepository clientRepository;
    private final ShowtimeRepository showtimeRepository;
    private final TicketRepository ticketRepository;
    private final TicketSeatRepository ticketSeatRepository;
    private final PaymentRepository paymentRepository;
    private final BankRepository bankRepository;
    private final SeatRepository seatRepository;
    private final TheaterRepository theaterRepository;
    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    public TicketService(ClientRepository clientRepository, ShowtimeRepository showtimeRepository, TicketRepository ticketRepository, TicketSeatRepository ticketSeatRepository, PaymentRepository paymentRepository, BankRepository bankRepository, SeatRepository seatRepository, TheaterRepository theaterRepository, PartnerRepository partnerRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.showtimeRepository = showtimeRepository;
        this.ticketRepository = ticketRepository;
        this.ticketSeatRepository = ticketSeatRepository;
        this.paymentRepository = paymentRepository;
        this.bankRepository = bankRepository;
        this.seatRepository = seatRepository;
        this.theaterRepository = theaterRepository;
        this.partnerRepository = partnerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 티켓 예매
    @Transactional
    public void reserveTicket(HttpServletRequest request, TicketReserveFormDTO ticketReserveFormDTO) {

        // 세션의 존재 여부로 회원 비회원 구분
        HttpSession session = request.getSession(false);
        String requestDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 비회원의 경우
        if (session == null) {

            // 비회원 저장
            Client notMember = new Client();
            notMember.setName(ticketReserveFormDTO.getPrivateInfoDTO().getName());
            notMember.setSecurityNm(passwordEncoder.encode(ticketReserveFormDTO.getPrivateInfoDTO().getSecurityNm()));
            notMember.setPhoneNm(ticketReserveFormDTO.getPrivateInfoDTO().getPhoneNm());
            notMember.setIsMember('N');

            clientRepository.save(notMember);

            // showtime 찾아서 ticket 저장
            Showtime showtime = showtimeRepository.findShowtimeById((long) ticketReserveFormDTO.getShowtimeId());

            Ticket ticket = new Ticket();
            ticket.setPrice(ticketReserveFormDTO.getPrice());
            ticket.setPaymentDate(requestDateTime);
            ticket.setStatus(TicketStatus.STANDBY);
            ticket.setUsedPoint(ticketReserveFormDTO.getUsedPoint());
            ticket.setReserveNm(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
            ticket.setUpdatedAt(requestDateTime);
            ticket.setShowtime(showtime);
            ticket.setClient(notMember);

            ticketRepository.save(ticket);

            // ticket_seat 에 내역 저장
            for (SeatDTO seatDTO : ticketReserveFormDTO.getSeats()) {
                Seat seat = seatRepository.findSeatBySeatNmAndTheater(seatDTO.getSeatNm(), theaterRepository.findTheaterById((long) seatDTO.getTheaterId()));

                TicketSeat ticketSeat = new TicketSeat();
                ticketSeat.setSeat(seat);
                ticketSeat.setTicket(ticket);

                ticketSeatRepository.save(ticketSeat);
            }

            // 결제 방식(계좌, 카드)에 따라 payment 를 다르게 저장
            if (ticketReserveFormDTO.getPaymentMethod().equals(PaymentMethod.ACCOUNT)) {

                // 제 3의 서비스에 내역 저장
                Bank bank = new Bank();
                bank.setSource(ticketReserveFormDTO.getCardOrAccountNm());
                bank.setDestination(ScvConst.SCV_ACCOUNT);
                bank.setPrice(ticketReserveFormDTO.getPrice());
                bank.setCreatedAt(requestDateTime);
                bank.setUpdatedAt(requestDateTime);
                bank.setStatus(BankStatus.STANDBY);
                bank.setMethod(PaymentMethod.ACCOUNT);

                bankRepository.save(bank);

                // payment 저장
                Payment payment = new Payment();
                payment.setMethod(PaymentMethod.ACCOUNT);
                payment.setBankId(bank.getId().intValue());
                payment.setTicket(ticket);
                payment.setPartner(partnerRepository.findPartnerByName(ticketReserveFormDTO.getPartnerName()));

                paymentRepository.save(payment);
            } else if (ticketReserveFormDTO.getPaymentMethod().equals(PaymentMethod.CARD)) {

                // 카드 결제시 티켓이 결제 상태로 생성된다.
                ticket.setStatus(TicketStatus.PAYED);
                ticketRepository.save(ticket);

                String approveNm = UUID.randomUUID().toString().replace("-", "").substring(0, 16);

                // 제 3의 서비스에 내역 저장
                Bank bank = new Bank();
                bank.setApproveNm(approveNm);
                bank.setSource(ticketReserveFormDTO.getCardOrAccountNm());
                bank.setDestination(ScvConst.SCV_ACCOUNT);
                bank.setPrice(ticketReserveFormDTO.getPrice());
                bank.setCreatedAt(requestDateTime);
                bank.setUpdatedAt(requestDateTime);
                bank.setStatus(BankStatus.APPROVED);
                bank.setMethod(PaymentMethod.CARD);

                bankRepository.save(bank);

                // payment 저장
                Payment payment = new Payment();
                payment.setMethod(PaymentMethod.CARD);
                payment.setBankId(bank.getId().intValue());
                payment.setTicket(ticket);
                payment.setApproveNm(approveNm);
                payment.setPartner(partnerRepository.findPartnerByName(ticketReserveFormDTO.getPartnerName()));

                paymentRepository.save(payment);
            }

            // 상영 일정 is_sold_out 갱신
            int remainedSeatNm = 0;

            for (Seat seat : seatRepository.findAllByTheater(showtime.getTheater())) {
                if (!ticketSeatRepository.existsBySeat(seat)) {
                    remainedSeatNm++;
                }
            }

            if (remainedSeatNm == 0) {
                showtime.setIsSoldOut('Y');
                showtimeRepository.save(showtime);
            }
        } else { // 회원인 경우
            Client loginMember = (Client) session.getAttribute(SessionConst.LOGIN_MEMBER);

            // showtime 찾아서 ticket 저장
            Showtime showtime = showtimeRepository.findShowtimeById((long) ticketReserveFormDTO.getShowtimeId());

            Ticket ticket = new Ticket();
            ticket.setPrice(ticketReserveFormDTO.getPrice());
            ticket.setPaymentDate(requestDateTime);
            ticket.setStatus(TicketStatus.STANDBY);
            ticket.setUsedPoint(ticketReserveFormDTO.getUsedPoint());
            ticket.setReserveNm(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
            ticket.setUpdatedAt(requestDateTime);
            ticket.setShowtime(showtime);
            ticket.setClient(loginMember);

            ticketRepository.save(ticket);

            // ticket_seat 에 내역 저장
            for (SeatDTO seatDTO : ticketReserveFormDTO.getSeats()) {
                Seat seat = seatRepository.findSeatBySeatNmAndTheater(seatDTO.getSeatNm(), theaterRepository.findTheaterById((long) seatDTO.getTheaterId()));

                TicketSeat ticketSeat = new TicketSeat();
                ticketSeat.setSeat(seat);
                ticketSeat.setTicket(ticket);

                ticketSeatRepository.save(ticketSeat);
            }

            // 결제 방식(계좌, 카드, 포인트)에 따라 payment 를 다르게 저장
            if (ticketReserveFormDTO.getPaymentMethod().equals(PaymentMethod.ACCOUNT)) {

                // 제 3의 서비스에 내역 저장
                Bank bank = new Bank();
                bank.setSource(ticketReserveFormDTO.getCardOrAccountNm());
                bank.setDestination(ScvConst.SCV_ACCOUNT);
                bank.setPrice(ticketReserveFormDTO.getPrice());
                bank.setCreatedAt(requestDateTime);
                bank.setUpdatedAt(requestDateTime);
                bank.setStatus(BankStatus.STANDBY);
                bank.setMethod(PaymentMethod.ACCOUNT);

                bankRepository.save(bank);

                // payment 저장
                Payment payment = new Payment();
                payment.setMethod(PaymentMethod.ACCOUNT);
                payment.setBankId(bank.getId().intValue());
                payment.setTicket(ticket);
                payment.setPartner(partnerRepository.findPartnerByName(ticketReserveFormDTO.getPartnerName()));

                paymentRepository.save(payment);

                // 10%에 해당하는 금액을 포인트로 적립하고 이에 맞는 등급 부여
                loginMember.setPoint(loginMember.getPoint() - ticketReserveFormDTO.getUsedPoint() + Double.valueOf(ticketReserveFormDTO.getPrice() * 0.1).intValue());
                loginMember.setMembership(returnUserLevelByAccumulatedPrintedTicketNm(loginMember));

                clientRepository.save(loginMember);

            } else if (ticketReserveFormDTO.getPaymentMethod().equals(PaymentMethod.CARD)) {

                // 카드 결제시 티켓이 결제 상태로 생성된다.
                ticket.setStatus(TicketStatus.PAYED);
                ticketRepository.save(ticket);

                String approveNm = UUID.randomUUID().toString().replace("-", "").substring(0, 16);

                // 제 3의 서비스에 내역 저장
                Bank bank = new Bank();
                bank.setApproveNm(approveNm);
                bank.setSource(ticketReserveFormDTO.getCardOrAccountNm());
                bank.setDestination(ScvConst.SCV_ACCOUNT);
                bank.setPrice(ticketReserveFormDTO.getPrice());
                bank.setCreatedAt(requestDateTime);
                bank.setUpdatedAt(requestDateTime);
                bank.setStatus(BankStatus.APPROVED);
                bank.setMethod(PaymentMethod.CARD);

                bankRepository.save(bank);

                // payment 저장
                Payment payment = new Payment();
                payment.setMethod(PaymentMethod.CARD);
                payment.setBankId(bank.getId().intValue());
                payment.setTicket(ticket);
                payment.setApproveNm(approveNm);
                payment.setPartner(partnerRepository.findPartnerByName(ticketReserveFormDTO.getPartnerName()));

                paymentRepository.save(payment);

                // 10%에 해당하는 금액을 포인트로 적립하고 이에 맞는 등급 부여
                loginMember.setPoint(loginMember.getPoint() - ticketReserveFormDTO.getUsedPoint() + Double.valueOf(ticketReserveFormDTO.getPrice() * 0.1).intValue());
                loginMember.setMembership(returnUserLevelByAccumulatedPrintedTicketNm(loginMember));

                clientRepository.save(loginMember);
            } else if (ticketReserveFormDTO.getPaymentMethod().equals(PaymentMethod.POINT)) {

                // 포인트로 결제 시, 티켓 상태는 바로 PAYED
                ticket.setStatus(TicketStatus.PAYED);
                ticketRepository.save(ticket);

                // payment 저장
                Payment payment = new Payment();
                payment.setMethod(PaymentMethod.POINT);
                payment.setTicket(ticket);
                payment.setPartner(partnerRepository.findPartnerByName(ticketReserveFormDTO.getPartnerName()));

                paymentRepository.save(payment);

                // 포인트 차감
                loginMember.setPoint(loginMember.getPoint() - ticketReserveFormDTO.getUsedPoint());

                // 10%에 해당하는 금액을 포인트로 적립하고 이에 맞는 등급 부여
                loginMember.setPoint(loginMember.getPoint() - ticketReserveFormDTO.getUsedPoint() + Double.valueOf(ticketReserveFormDTO.getPrice() * 0.1).intValue());
                loginMember.setMembership(returnUserLevelByAccumulatedPrintedTicketNm(loginMember));

                clientRepository.save(loginMember);
            }

            // 상영 일정 is_sold_out 갱신
            int remainedSeatNm = 0;

            for (Seat seat : seatRepository.findAllByTheater(showtime.getTheater())) {
                if (!ticketSeatRepository.existsBySeat(seat)) {
                    remainedSeatNm++;
                }
            }

            if (remainedSeatNm == 0) {
                showtime.setIsSoldOut('Y');
                showtimeRepository.save(showtime);
            }
        }
    }

    // 누적 결제 금액으로 회원 등급 반환
    public String returnUserLevelByAccumulatedPrintedTicketNm(Client client) {
        int accumulatedPrintedTicketNm = 0;

        for (Ticket ticket : ticketRepository.findAllByClientId(client.getId())) {
            if (ticket.getStatus().equals(TicketStatus.PRINTED)) {
                accumulatedPrintedTicketNm++;
            }
        }

        if (accumulatedPrintedTicketNm < ClientLevel.VIP_LIMIT) {
            return UserLevel.COMMON;
        } else if (accumulatedPrintedTicketNm < ClientLevel.VVIP_LIMIT) {
            return UserLevel.VIP;
        } else {
            return UserLevel.VVIP;
        }
    }

    // 티켓 취소
    @Transactional
    public void cancelTicket(String ticketId) { // TODO: ticket_seat에서 삭제할 것 + is_sold_out 갱신

        // 영화가 상영되지 않았으면 취소가 가능
        String requestDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Ticket ticket = ticketRepository.findTicketById(Long.valueOf(ticketId));

        if (ticket == null) {
            throw new IllegalArgumentException("취소할 티켓이 존재하지 않습니다.");
        }

        // startDate 형식 -> yyyy-MM-dd HH:mm
        String startDate = ticket.getShowtime().getStartDate();

        LocalDateTime requestDateTimeObj = LocalDateTime.parse(requestDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime startDateTimeObj = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        if (requestDateTimeObj.isAfter(startDateTimeObj)) {
            throw new IllegalArgumentException("이미 상영 중인 영화에 대한 티켓은 취소할 수 없습니다.");
        }

        // ticket 의 상태를 CANCELLED 로 수정
        ticket.setStatus(TicketStatus.CANCELLED);
        ticket.setUpdatedAt(requestDateTime);
        ticketRepository.save(ticket);

        // ticket_seat 에서 row 삭제
        ticketSeatRepository.deleteAllByTicket(ticket);

        // 포인트 반환
        Client client = ticket.getClient();
        client.setPoint(client.getPoint() + ticket.getUsedPoint());
        clientRepository.save(client);
    }

    // 예매번호로 티켓 조회
    public TicketDTO checkTicketByReserveNm(String reserveNm) {

        Ticket ticket = ticketRepository.findTicketByReserveNm(reserveNm);

        if (ticket == null) {
            throw new IllegalArgumentException("해당 번호로 된 예매 티켓이 없습니다.");
        }

        StringBuilder seatInfoBuilder = new StringBuilder();
        for (TicketSeat ticketSeat : ticketSeatRepository.findAllByTicket(ticket)) {
            seatInfoBuilder.append(ticketSeat.getSeat().getSeatNm()).append(" ");
        }
        String seatInfo = seatInfoBuilder.toString().trim();

        // 서비스 단에서 넣을 것 주입
        TicketDTO ticketDTO = TicketDTO.from(ticket);
        ticketDTO.setPeopleNm(ticketSeatRepository.findAllByTicket(ticket).size());
        ticketDTO.setSeatInfo(seatInfo);
        ticketDTO.setPaymentMethod(paymentRepository.findPaymentByTicket(ticket).getMethod());

        return ticketDTO;
    }

    // 개인 정보로 티켓 리스트 조회
    public List<TicketDTO> checkTicketByInfo(TicketCheckFormDTO ticketCheckFormDTO) {

        Client client = clientRepository.findClientByNameAndPhoneNm(ticketCheckFormDTO.getName(), ticketCheckFormDTO.getPhoneNm());

        if (client == null) {
            throw new IllegalArgumentException("제공해주신 정보가 잘못된 정보입니다.");
        }

        List<TicketDTO> ticketDTOList = new ArrayList<>();
        for (Ticket ticket : ticketRepository.findAllByClientId(client.getId())) {

            StringBuilder seatInfoBuilder = new StringBuilder();
            for (TicketSeat ticketSeat : ticketSeatRepository.findAllByTicket(ticket)) {
                seatInfoBuilder.append(ticketSeat.getSeat().getSeatNm()).append(" ");
            }
            String seatInfo = seatInfoBuilder.toString().trim();

            // 서비스 단에서 넣을 것 주입
            TicketDTO ticketDTO = TicketDTO.from(ticket);
            ticketDTO.setPeopleNm(ticketSeatRepository.findAllByTicket(ticket).size());
            ticketDTO.setSeatInfo(seatInfo);
            ticketDTO.setPaymentMethod(paymentRepository.findPaymentByTicket(ticket).getMethod());

            ticketDTOList.add(ticketDTO);
        }

        return ticketDTOList;
    }

    // 예매 티켓 리스트 조회
    public List<TicketDTO> checkTickets(HttpServletRequest request) {

        Client loginMember = (Client) request.getSession(false).getAttribute(SessionConst.LOGIN_MEMBER);

        if (loginMember.getIsMember() == 'N') {
            throw new IllegalArgumentException("회원이 아닌 경우, 사용할 수 없는 기능입니다.");
        }

        List<TicketDTO> ticketDTOList = new ArrayList<>();
        for (Ticket ticket : ticketRepository.findAllByClientId(loginMember.getId())) {

            StringBuilder seatInfoBuilder = new StringBuilder();
            for (TicketSeat ticketSeat : ticketSeatRepository.findAllByTicket(ticket)) {
                seatInfoBuilder.append(ticketSeat.getSeat().getSeatNm()).append(" ");
            }
            String seatInfo = seatInfoBuilder.toString().trim();

            // 서비스 단에서 넣을 것 주입
            TicketDTO ticketDTO = TicketDTO.from(ticket);
            ticketDTO.setPeopleNm(ticketSeatRepository.findAllByTicket(ticket).size());
            ticketDTO.setSeatInfo(seatInfo);
            ticketDTO.setPaymentMethod(paymentRepository.findPaymentByTicket(ticket).getMethod());

            ticketDTOList.add(ticketDTO);
        }

        return ticketDTOList;
    }

    // 모든 티켓 조회 (어드민)
    public List<TicketDTO> showTickets(HttpServletRequest request) {

        Admin loginAdmin = (Admin) request.getSession(false).getAttribute(SessionConst.LOGIN_MEMBER);

        if (loginAdmin == null) {
            throw new IllegalArgumentException("어드민이 아닌 경우, 사용할 수 없는 기능입니다.");
        }

        List<TicketDTO> ticketDTOList = new ArrayList<>();
        for (Ticket ticket : ticketRepository.findAll()) {

            StringBuilder seatInfoBuilder = new StringBuilder();
            for (TicketSeat ticketSeat : ticketSeatRepository.findAllByTicket(ticket)) {
                seatInfoBuilder.append(ticketSeat.getSeat().getSeatNm()).append(" ");
            }
            String seatInfo = seatInfoBuilder.toString().trim();

            // 서비스 단에서 넣을 것 주입
            TicketDTO ticketDTO = TicketDTO.from(ticket);
            ticketDTO.setPeopleNm(ticketSeatRepository.findAllByTicket(ticket).size());
            ticketDTO.setSeatInfo(seatInfo);
            ticketDTO.setPaymentMethod(paymentRepository.findPaymentByTicket(ticket).getMethod());

            ticketDTOList.add(ticketDTO);
        }

        return ticketDTOList;
    }

    // 티켓 출력하기
    @Transactional
    public void printTicket(String ticketId) {

        Ticket ticket = ticketRepository.findTicketById(Long.valueOf(ticketId));

        // null 체크
        if (ticket == null) {
            throw new IllegalArgumentException("출력할 티켓이 존재하지 않습니다.");
        }

        if (!ticket.getStatus().equals(TicketStatus.PAYED)) {
            throw new IllegalArgumentException("PAYED 상태가 아닌 경우 티켓 발권은 불가능합니다.");
        }

        // 발권 상태로 변경
        ticket.setStatus(TicketStatus.PRINTED);
        ticketRepository.save(ticket);
    }
}
