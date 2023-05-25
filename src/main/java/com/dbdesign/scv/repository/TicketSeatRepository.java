package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Seat;
import com.dbdesign.scv.entity.Ticket;
import com.dbdesign.scv.entity.TicketSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketSeatRepository extends JpaRepository<TicketSeat, Long> {

    boolean existsBySeat(Seat seat);

    List<TicketSeat> findAllByTicket(Ticket ticket);

    void deleteAllByTicket(Ticket ticket);
}
