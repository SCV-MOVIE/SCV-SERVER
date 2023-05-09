package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByClientId(Long clientId);
}
