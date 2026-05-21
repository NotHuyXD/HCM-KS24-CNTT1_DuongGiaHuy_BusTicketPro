package com.bus.config;

import com.bus.entity.Ticket;
import com.bus.repository.TicketRepository;
import com.bus.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class TicketCleanupJob {

    @Autowired private TicketRepository ticketRepo;
    @Autowired private BookingService bookingService;

    @Scheduled(fixedRate = 600000)
    public void cleanupExpiredTickets() {
        System.out.println("⏱ [CRON JOB] Đang quét các vé chưa thanh toán quá hạn...");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -30);
        Date cutoffTime = cal.getTime();

        List<Ticket> expiredTickets = ticketRepo.findExpiredPendingTickets(cutoffTime);

        int count = 0;
        for (Ticket ticket : expiredTickets) {
            try {
                bookingService.cancelTicket(ticket.getId(), false);
                count++;
            } catch (Exception e) {
                System.out.println("Lỗi hủy vé tự động ID " + ticket.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("✅ [CRON JOB] Quét xong. Đã tự động hủy " + count + " vé quá hạn giữ chỗ.");
    }

    @Scheduled(fixedRate = 900000)
    public void cleanupDepartedBusTickets() {
        System.out.println("⏰ [CRON JOB - SCHEDULE] Đang kiểm tra danh sách chuyến xe đã khởi hành...");

        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        List<Ticket> missedTickets = ticketRepo.findPendingTicketsOfDepartedBuses(now);

        int count = 0;
        for (Ticket ticket : missedTickets) {
            try {
                bookingService.cancelTicket(ticket.getId(), false);
                count++;
            } catch (Exception e) {
                System.out.println("Lỗi xử lý vé quá hạn ID " + ticket.getId() + ": " + e.getMessage());
            }
        }

        if(count > 0) {
            System.out.println("✅ [CRON JOB] Đã tự động hủy " + count + " vé do xe đã chạy nhưng hành khách không thanh toán.");
        } else {
            System.out.println("🔹 [CRON JOB] Không có vé nào bị quá hạn giờ khởi hành.");
        }
    }
}