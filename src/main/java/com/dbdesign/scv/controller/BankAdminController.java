package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.BankDTO;
import com.dbdesign.scv.dto.HandleTicketDTO;
import com.dbdesign.scv.dto.LoginDTO;
import com.dbdesign.scv.service.BankAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/bankAdmin/")
public class BankAdminController {

    private final BankAdminService bankAdminService;

    public BankAdminController(BankAdminService bankAdminService) {
        this.bankAdminService = bankAdminService;
    }

    // 로그인
    @PostMapping("/member/login")
    @ApiOperation(value = "로그인", notes = "아이디와 비밀번호로 로그인합니다.")
    public ResponseEntity<Void> login(@RequestBody LoginDTO loginDTO) {

        bankAdminService.login(loginDTO);
        return ResponseEntity.ok().build();
    }

    // 로그아웃
    @PostMapping("/member/logout")
    @ApiOperation(value = "로그아웃", notes = "로그아웃하며 세션와 쿠키를 초기화합니다.")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        bankAdminService.logout(request, response);
        return ResponseEntity.ok().build();
    }

    // 계좌 이체 요청에 대한 승인 또는 거절 (뱅크어드민)
    @PatchMapping("/handle/ticket")
    @ApiOperation(value = "계좌 이체 요청에 대한 승인 또는 거절 (뱅크어드민)", notes = "계좌 이체 요청에 대한 승인 또는 거절을 합니다." +
            "\n이에 따라 payment 와 ticket 의 상태 또한 바뀌며 거절한 경우, 티켓의 금액이 포인트로 전환됩니다.")
    public ResponseEntity<Void> handleTicket(@RequestBody HandleTicketDTO handleTicketDTO) {

        bankAdminService.handleTicket(handleTicketDTO);
        return ResponseEntity.ok().build();
    }

    // 모든 결제 내역 조회 (뱅크 어드민)
    @GetMapping("/bank/list")
    @ApiOperation(value = "모든 결제 내역 조회 (뱅크 어드민)", notes = "뱅크 어드민이 아닌 경우, 사용할 수 없는 기능입니다.")
    public ResponseEntity<List<BankDTO>> showBanks() {

        return ResponseEntity.ok().body(bankAdminService.showBanks());
    }
}
