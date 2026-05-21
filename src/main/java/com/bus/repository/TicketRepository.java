package com.bus.repository;

import com.bus.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t JOIN t.user u JOIN t.seat s JOIN s.bus b JOIN b.route r " +
            "WHERE t.ticketCode = :code AND u.phone = :phone")
    Optional<Ticket> findByCodeAndPhone(@Param("code") String ticketCode, @Param("phone") String phone);

    long countByStatus(Ticket.TicketStatus status);

    @Query("SELECT SUM(t.seat.bus.route.basePrice) FROM Ticket t WHERE t.status = :status")
    Double calculateTotalRevenue(@Param("status") Ticket.TicketStatus status);

    List<Ticket> findByUserOrderByBookingTimeDesc(com.bus.entity.User user);

    @Query("SELECT t FROM Ticket t WHERE t.status = 'PENDING' AND t.bookingTime < :cutoffTime")
    List<Ticket> findExpiredPendingTickets(@Param("cutoffTime") java.util.Date cutoffTime);

}