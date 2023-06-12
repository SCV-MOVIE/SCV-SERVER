package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.LoginDTO;
import com.dbdesign.scv.dto.UpdateUserInfoByAdminDTO;
import com.dbdesign.scv.dto.UpdateUserInfoDTO;
import com.dbdesign.scv.service.AdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 회원 정보 수정 (어드민)
    @PatchMapping("/member/info")
    @ApiOperation(value = "회원 정보 수정", notes = "회원등급, 포인트를 수정할 수 있습니다.")
    public ResponseEntity<Void> updateUserInfo(@RequestBody UpdateUserInfoByAdminDTO updateUserInfoByAdminDTO) {

        adminService.updateUserInfo(updateUserInfoByAdminDTO);
        return ResponseEntity.ok().build();
    }
}
