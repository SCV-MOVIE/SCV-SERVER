package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.PartnerDTO;
import com.dbdesign.scv.dto.PartnerFormDTO;
import com.dbdesign.scv.service.PartnerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/partner")
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    // 제휴사 등록 (어드민)
    @PostMapping
    @ApiOperation(value = "제휴사 등록 (어드민)", notes = "제휴사 이름과 할인 금액을 기입합니다.")
    public ResponseEntity<Void> registerPartner(HttpServletRequest request, @RequestBody PartnerFormDTO partnerFormDTO) {

        partnerService.registerPartner(request, partnerFormDTO);
        return ResponseEntity.ok().build();
    }

    // 제휴사 리스트 조회
    @GetMapping("/list")
    @ApiOperation(value = "모든 제휴사 리스트 조회", notes = "모든 제휴사 리스트를 반환합니다.")
    public ResponseEntity<List<PartnerDTO>> showPartners() {

        return ResponseEntity.ok().body(partnerService.showPartners());
    }
}
