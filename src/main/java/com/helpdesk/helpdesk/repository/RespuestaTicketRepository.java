package com.helpdesk.helpdesk.repository;

import com.helpdesk.helpdesk.model.RespuestaTicket;
import com.helpdesk.helpdesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespuestaTicketRepository extends JpaRepository<RespuestaTicket, Long> {
    List<RespuestaTicket> findByTicket(Ticket ticket);
}
