package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.TicketCheckFormDTO;
import com.dbdesign.scv.dto.TicketDTO;
import com.dbdesign.scv.dto.TicketReserveFormDTO;
import com.dbdesign.scv.service.TicketService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // 티켓 예매
    @PostMapping("/reserve")
    @ApiOperation(value = "티켓 예매", notes = "티켓 예매를 통해 포인트가 적립되고 회원등급이 바뀔 수 있습니다.")
    public ResponseEntity<Void> reserveTicket(HttpServletRequest request, @RequestBody TicketReserveFormDTO ticketReserveFormDTO) {

        ticketService.reserveTicket(request, ticketReserveFormDTO);
        return ResponseEntity.ok().build();
    }

    // 티켓 취소
    @PatchMapping("/cancel/{ticketId}")
    @ApiOperation(value = "티켓 취소", notes = "영화가 상영되지 않았으면 취소가 가능합니다. 취소가 되면 티켓의 금액이 포인트로 전환됩니다.")
    public ResponseEntity<Void> cancelTicket(@PathVariable String ticketId) {

        ticketService.cancelTicket(ticketId);
        return ResponseEntity.ok().build();
    }

    // 예매번호로 티켓 조회
    @GetMapping("/check-by/reserveNm/{reserveNm}")
    @ApiOperation(value = "예매번호로 티켓 조회", notes = "16자리 예매번호로 티켓을 조회합니다.")
    public ResponseEntity<TicketDTO> checkTicketByReserveNm(@PathVariable String reserveNm) {

        return ResponseEntity.ok().body(ticketService.checkTicketByReserveNm(reserveNm));
    }

    // 개인 정보로 티켓 리스트 조회 (회원에게는 불필요한 기능)
    @GetMapping("/check-by/info")
    @ApiOperation(value = "개인 정보로 티켓 리스트 조회 (회원에게는 불필요한 기능)", notes = "이름, 주민번호, 핸드폰 번호로 티켓 리스트를 조회합니다.")
    public ResponseEntity<List<TicketDTO>> checkTicketByInfo(@RequestBody TicketCheckFormDTO ticketCheckFormDTO) {

        return ResponseEntity.ok().body(ticketService.checkTicketByInfo(ticketCheckFormDTO));
    }

    // 예매 티켓 리스트 조회
    @GetMapping("/list")
    @ApiOperation(value = "예매 티켓 리스트 조회", notes = "회원이 아닌 경우, 사용할 수 없는 기능입니다.")
    public ResponseEntity<List<TicketDTO>> checkTickets(HttpServletRequest request) {

        return ResponseEntity.ok().body(ticketService.checkTickets(request));
    }

    // 모든 티켓 조회 (어드민)
    @GetMapping("/admin/list")
    @ApiOperation(value = "예매 티켓 리스트 조회", notes = "어드민이 아닌 경우, 사용할 수 없는 기능입니다.")
    public ResponseEntity<List<TicketDTO>> showTickets(HttpServletRequest request) {

        return ResponseEntity.ok().body(ticketService.showTickets(request));
    }

    // 티켓 출력하기
    @PatchMapping("/print/{ticketId}")
    @ApiOperation(value = "티켓 출력", notes = "발권시 티켓이 PRINTED 상태로 변하며 PAYED 상태가 아닌 경우 티켓 발권은 불가능합니다.")
    public ResponseEntity<Void> printTicket(@PathVariable String ticketId) {

        ticketService.printTicket(ticketId);
        return ResponseEntity.ok().build();
    }
}
