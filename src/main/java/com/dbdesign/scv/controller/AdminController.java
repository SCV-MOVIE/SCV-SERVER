package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.LoginDTO;
import com.dbdesign.scv.service.AdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/admin/")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 로그인
    @PostMapping("/member/login")
    @ApiOperation(value = "로그인", notes = "아이디와 비밀번호로 로그인합니다.")
    public ResponseEntity<Void> login(@RequestBody LoginDTO loginDTO) {

        adminService.login(loginDTO);
        return ResponseEntity.ok().build();
    }

    // 로그아웃
    @PostMapping("/member/logout")
    @ApiOperation(value = "로그아웃", notes = "로그아웃하며 세션와 쿠키를 초기화합니다.")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        adminService.logout(request, response);
        return ResponseEntity.ok().build();
    }
}
