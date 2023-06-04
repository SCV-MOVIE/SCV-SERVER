package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.PartnerDTO;
import com.dbdesign.scv.dto.PartnerFormDTO;
import com.dbdesign.scv.entity.Partner;
import com.dbdesign.scv.repository.PartnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void registerPartner(PartnerFormDTO partnerFormDTO) {

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
