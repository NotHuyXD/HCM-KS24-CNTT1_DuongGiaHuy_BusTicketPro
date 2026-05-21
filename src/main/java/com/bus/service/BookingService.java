package com.bus.service;

import com.bus.entity.Seat;
import com.bus.entity.Ticket;
import com.bus.entity.User;
import com.bus.repository.SeatRepository;
import com.bus.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired private TicketRepository ticketRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private EmailService emailService;

    @Transactional(rollbackFor = Exception.class)
    public Ticket bookTicket(User user, Long seatId) throws Exception {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new Exception("Không tìm thấy ghế!"));

        if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
            throw new Exception("Ghế này đã có người đặt hoặc đang chờ thanh toán!");
        }

        seat.setStatus(Seat.SeatStatus.PENDING);
        seatRepository.save(seat);

        Ticket ticket = new Ticket();
        ticket.setTicketCode("VN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        ticket.setUser(user);
        ticket.setSeat(seat);
        ticket.setStatus(Ticket.TicketStatus.PENDING);
        ticket.setBookingTime(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(ticket);

        String routeInfo = seat.getBus().getRoute().getStartLocation() + " - " + seat.getBus().getRoute().getEndLocation();
        String seatInfo = "Biển số " + seat.getBus().getPlateNumber() + " - Ghế " + seat.getSeatName();

        emailService.sendBookingConfirmation(user.getEmail(), savedTicket.getTicketCode(), routeInfo, seatInfo);

        return savedTicket;
    }

    @Transactional
    public void confirmPayment(Long ticketId) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new Exception("Không tìm thấy vé!"));

        ticket.setStatus(Ticket.TicketStatus.PAID);
        ticket.getSeat().setStatus(Seat.SeatStatus.BOOKED);

        ticketRepository.save(ticket);
        seatRepository.save(ticket.getSeat());
    }

    @Transactional
    public void cancelTicket(Long ticketId, boolean isPassenger) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new Exception("Không tìm thấy vé!"));

        if (isPassenger) {
        }

        ticket.setStatus(Ticket.TicketStatus.CANCELLED);
        ticket.getSeat().setStatus(Seat.SeatStatus.AVAILABLE);

        ticketRepository.save(ticket);
        seatRepository.save(ticket.getSeat());
    }
}