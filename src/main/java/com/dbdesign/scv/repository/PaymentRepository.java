package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Payment;
import com.dbdesign.scv.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findPaymentByTicket(Ticket ticket);

    Payment findPaymentByBankId(int bankId);
}
