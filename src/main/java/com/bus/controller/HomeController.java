package com.bus.controller;

import com.bus.entity.Bus;
import com.bus.entity.User;
import com.bus.repository.BusRepository;
import com.bus.repository.SeatRepository;
import com.bus.repository.TicketRepository;
import com.bus.repository.UserRepository;
import com.bus.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Thêm thư viện này
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @Autowired
    private TicketRepository ticketRepo;

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null) {
            if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))) {
                return "redirect:/admin/dashboard";
            }
            if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("STAFF"))) {
                return "redirect:/staff/tickets";
            }
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

    @Autowired private BusRepository busRepo;
    @Autowired private SeatRepository seatRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private BookingService bookingService;

    // 1. TÌM KIẾM CHUYẾN XE
    @GetMapping("/search-bus")
    public String searchBus(@RequestParam String start, @RequestParam String end, Model model) {
        model.addAttribute("buses", busRepo.findByRoute_StartLocationAndRoute_EndLocation(start, end));
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        return "home"; // Trả về lại trang chủ cùng danh sách kết quả
    }

    // 2. XEM SƠ ĐỒ GHẾ CỦA TỪNG XE (CORE-05)
    @GetMapping("/bus/{id}/seats")
    public String viewSeatMap(@PathVariable Long id, Model model) {
        Bus bus = busRepo.findById(id).orElseThrow();
        model.addAttribute("bus", bus);
        model.addAttribute("seats", seatRepo.findByBusId(id));
        return "seat-map";
    }

    // 3. XEM LỊCH SỬ ĐẶT VÉ CÁ NHÂN
    @GetMapping("/my-tickets")
    public String myTickets(Authentication auth, Model model) {
        if (auth == null) return "redirect:/login";
        User user = userRepo.findByUsername(auth.getName()).orElseThrow();
        model.addAttribute("myTickets", ticketRepo.findByUserOrderByBookingTimeDesc(user));
        return "my-tickets";
    }

    // 4. HÀNH KHÁCH CHỦ ĐỘNG HỦY VÉ (CORE-09)
    @PostMapping("/cancel-ticket/{id}")
    public String passengerCancelTicket(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            // isPassenger = true để áp dụng luật hủy vé của hành khách
            bookingService.cancelTicket(id, true);
            attrs.addFlashAttribute("success", "Đã hủy vé thành công!");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/my-tickets";
    }
}