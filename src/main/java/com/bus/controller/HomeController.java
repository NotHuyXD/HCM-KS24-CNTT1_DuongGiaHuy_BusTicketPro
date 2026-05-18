package com.bus.controller;

import com.bus.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Thêm thư viện này
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private TicketRepository ticketRepo;

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ADMIN"))) {

            return "redirect:/admin/dashboard";
        }

        return "home";
    }

    @GetMapping("/search-ticket")
    public String searchTicket(@RequestParam String ticketCode, @RequestParam String phone, Model model) {
        var ticketOpt = ticketRepo.findByCodeAndPhone(ticketCode, phone);
        if (ticketOpt.isPresent()) {
            model.addAttribute("ticket", ticketOpt.get());
        } else {
            model.addAttribute("notFound", "Không tìm thấy vé khớp với thông tin cung cấp.");
        }
        return "home";
    }
}