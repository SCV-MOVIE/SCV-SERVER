package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.BankAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAdminRepository extends JpaRepository<BankAdmin, Long> {

    BankAdmin findBankAdminByLoginId(String loginId);
}
