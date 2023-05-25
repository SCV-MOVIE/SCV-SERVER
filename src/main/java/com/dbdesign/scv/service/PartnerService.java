package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.PartnerDTO;
import com.dbdesign.scv.dto.PartnerFormDTO;
import com.dbdesign.scv.entity.Admin;
import com.dbdesign.scv.entity.Partner;
import com.dbdesign.scv.repository.PartnerRepository;
import com.dbdesign.scv.util.SessionConst;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    // 제휴사 등록 (어드민)
    @Transactional
    public void registerPartner(HttpServletRequest request, PartnerFormDTO partnerFormDTO) {

        Admin loginAdmin = (Admin) request.getSession(false).getAttribute(SessionConst.LOGIN_MEMBER);

        if (loginAdmin == null) {
            throw new IllegalArgumentException("어드민이 아닌 경우, 사용할 수 없는 기능입니다.");
        }

        // 새로운 제휴사 등록
        Partner newPartner = Partner.builder()
                .name(partnerFormDTO.getName())
                .discount(partnerFormDTO.getDiscount())
                .build();

        partnerRepository.save(newPartner);
    }

    // 제휴사 리스트 반환
    public List<PartnerDTO> showPartners() {

        List<PartnerDTO> partnerDTOList = new ArrayList<>();
        for (Partner partner : partnerRepository.findAll()) {

            PartnerDTO partnerDTO = PartnerDTO.from(partner);

            partnerDTOList.add(partnerDTO);
        }

        return partnerDTOList;
    }
}
