package com.bus.controller;

import com.bus.entity.Ticket;
import com.bus.entity.User;
import com.bus.repository.UserRepository;
import com.bus.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired private BookingService bookingService;
    @Autowired private UserRepository userRepository;

    @PostMapping("/book")
    public String bookTicket(@RequestParam Long seatId, Principal principal, RedirectAttributes redirectAttrs) {
        try {

            User currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();

            Ticket ticket = bookingService.bookTicket(currentUser, seatId);
            redirectAttrs.addFlashAttribute("success", "Đặt vé thành công! Mã vé: " + ticket.getTicketCode());
            return "redirect:/";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
}