package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findAdminByLoginId(String loginId);
}
