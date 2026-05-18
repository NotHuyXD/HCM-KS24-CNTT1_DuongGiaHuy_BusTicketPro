package com.bus.repository;

import com.bus.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t JOIN t.user u JOIN t.seat s JOIN s.bus b JOIN b.route r " +
            "WHERE t.ticketCode = :code AND u.phone = :phone")
    Optional<Ticket> findByCodeAndPhone(@Param("code") String ticketCode, @Param("phone") String phone);
}