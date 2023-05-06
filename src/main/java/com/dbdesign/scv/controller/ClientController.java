package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.*;
import com.dbdesign.scv.entity.Client;
import com.dbdesign.scv.service.ClientService;
import com.dbdesign.scv.util.SessionConst;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // 회원가입
    @PostMapping("/member/register")
    @ApiOperation(value = "회원 가입", notes = "회원가입을 진행합니다.")
    public ResponseEntity<?> registerUser(@Validated @RequestBody RegisterFormDTO registerFormDTO) {

        clientService.registerUser(registerFormDTO);
        return ResponseEntity.ok().build();
    }

    // 아이디 중복 확인
    @GetMapping("/member/duplication-check/loginId/{loginId}")
    @ApiOperation(value = "중복 체크", notes = "회원 가입 시 기입한 SCV 서비스 로그인 아이디가 중복되면 true를 반환합니다.")
    @ApiImplicitParam(
            name = "loginId"
            , value = "아이디"
            , required = true
            , dataType = "string"
            , paramType = "path"
            , defaultValue = "None"
            , example = "garfield")
    public ResponseEntity<Boolean> checkUidDuplicate(@PathVariable("loginId") String loginId) {
        return ResponseEntity.ok().body(clientService.checkLoginIdDuplicate(loginId));
    }

    // 로그인
    @PostMapping("/member/login")
    @ApiOperation(value = "로그인", notes = "아이디와 비밀번호로 로그인합니다.")
    public ResponseEntity<Void> login(@RequestBody LoginDTO loginDTO) {

        clientService.login(loginDTO);
        return ResponseEntity.ok().build();
    }

    // 회원 정보 수정
    @PatchMapping("/member/info")
    @ApiOperation(value = "회원 정보 수정", notes = "아이디, 이름, 전화번호를 수정할 수 있습니다.")
    public ResponseEntity<Void> updateUserInfo(@RequestBody UpdateUserInfoDTO updateUserInfoDTO) {

        clientService.updateUserInfo(updateUserInfoDTO);
        return ResponseEntity.ok().build();
    }

    // 회원 탈퇴
    @PatchMapping("/member/withdrawal")
    @ApiOperation(value = "회원 탈퇴", notes = "비밀번호 일치 시 회원 탈퇴가 가능합니다.")
    public ResponseEntity<Void> deleteUser(@RequestBody DeleteUserDTO deleteUserDTO) {

        clientService.deleteUser(deleteUserDTO);
        return ResponseEntity.ok().build();
    }

    // 모든 회원 정보 조회 (어드민 기능)
    @GetMapping("/member/list")
    @ApiOperation(value = "모든 회원 정보 조회", notes = "모든 회원 정보가 리스트 형태로 반환됩니다.")
    public ResponseEntity<List<ClientDTO>> getUserList() {

        return ResponseEntity.ok().body(clientService.getUserList());
    }

    // 현재 유저 정보 조회
    @GetMapping("/member/info")
    @ApiOperation(value = "현재 회원 정보 조회", notes = "현재 회원 정보가 반환됩니다.")
    public ResponseEntity<ClientDTO> getUserInfo(@ApiIgnore @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Client loginMember) {

        return ResponseEntity.ok().body(clientService.getUserInfo(loginMember));
    }
}
