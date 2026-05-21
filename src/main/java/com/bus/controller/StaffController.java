package com.bus.controller;

import com.bus.repository.TicketRepository;
import com.bus.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired private TicketRepository ticketRepo;
    @Autowired private BookingService bookingService;

    @GetMapping("/tickets")
    public String listTickets(Model model) {
        model.addAttribute("tickets", ticketRepo.findAll());
        return "staff/ticket-list";
    }

    @PostMapping("/tickets/approve/{id}")
    public String approveTicket(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            bookingService.confirmPayment(id);
            attrs.addFlashAttribute("success", "Đã duyệt thanh toán thành công cho mã vé này!");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/staff/tickets";
    }

    @PostMapping("/tickets/cancel/{id}")
    public String cancelTicket(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            bookingService.cancelTicket(id,false);
            attrs.addFlashAttribute("success", "Đã hủy vé và giải phóng tài nguyên ghế trống thành công!");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/staff/tickets";
    }
}