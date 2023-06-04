package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByLoginId(String loginId);

    Client findClientById(Long id);

    Client findClientByLoginId(String loginId);

    List<Client> findClientByNameAndPhoneNm(String name, String phoneNm);
}
