package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Partner findPartnerByName(String name);
}
