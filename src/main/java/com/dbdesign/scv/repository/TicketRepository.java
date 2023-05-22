package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Showtime;
import com.dbdesign.scv.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByClientId(Long clientId);

    List<Ticket> findAllByShowtime(Showtime showtime);

    Ticket findTicketById(Long ticketId);

    Ticket findTicketByReserveNm(String reserveNm);
}
