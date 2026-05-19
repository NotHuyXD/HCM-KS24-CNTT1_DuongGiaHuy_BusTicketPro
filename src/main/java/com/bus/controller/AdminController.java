package com.bus.controller;

import com.bus.entity.Bus;
import com.bus.entity.Route;
import com.bus.entity.Seat;
import com.bus.entity.Ticket;
import com.bus.repository.BusRepository;
import com.bus.repository.RouteRepository;
import com.bus.repository.SeatRepository;
import com.bus.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private BusRepository busRepo;
    @Autowired private RouteRepository routeRepo;
    @Autowired private TicketRepository ticketRepo;
    @Autowired private SeatRepository seatRepo;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/buses")
    public String listBuses(Model model) {
        model.addAttribute("buses", busRepo.findAll());
        return "admin/bus-list";
    }

    @GetMapping("/buses/add")
    public String showAddBusForm(Model model) {
        model.addAttribute("bus", new Bus());
        model.addAttribute("routes", routeRepo.findAll());
        return "admin/bus-form";
    }

    @GetMapping("/buses/edit/{id}")
    public String showEditBusForm(@PathVariable Long id, Model model) {
        Bus bus = busRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy xe!"));
        model.addAttribute("bus", bus);
        model.addAttribute("routes", routeRepo.findAll());
        return "admin/bus-form";
    }

    @PostMapping("/buses/save")
    public String saveBus(@ModelAttribute("bus") Bus bus) {
        boolean isNewBus = (bus.getId() == null);

        Bus savedBus = busRepo.save(bus);

        if (isNewBus) {
            int capacity = savedBus.getCapacity();
            for (int i = 1; i <= capacity; i++) {
                Seat seat = new Seat();
                seat.setSeatName("G" + i);
                seat.setStatus(Seat.SeatStatus.AVAILABLE);
                seat.setBus(savedBus);

                seatRepo.save(seat);
            }
        }

        return "redirect:/admin/buses";
    }

    @PostMapping("/buses/delete/{id}")
    public String deleteBus(@PathVariable Long id) {
        busRepo.deleteById(id);
        return "redirect:/admin/buses";
    }

    @GetMapping("/routes")
    public String listRoutes(Model model) {
        model.addAttribute("routes", routeRepo.findAll());
        return "admin/route-list";
    }

    @GetMapping("/routes/add")
    public String showAddRouteForm(Model model) {
        model.addAttribute("route", new Route());
        return "admin/route-form";
    }

    @GetMapping("/routes/edit/{id}")
    public String showEditRouteForm(@PathVariable Long id, Model model) {
        Route route = routeRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Tuyến đường không tồn tại!"));
        model.addAttribute("route", route);
        return "admin/route-form";
    }

    @PostMapping("/routes/save")
    public String saveRoute(@ModelAttribute("route") Route route) {
        routeRepo.save(route);
        return "redirect:/admin/routes";
    }

    @PostMapping("/routes/delete/{id}")
    public String deleteRoute(@PathVariable Long id) {
        routeRepo.deleteById(id);
        return "redirect:/admin/routes";
    }

    @GetMapping("/reports")
    public String viewReports(Model model) {
        long totalTickets = ticketRepo.count();
        long pendingTickets = ticketRepo.countByStatus(Ticket.TicketStatus.PENDING);
        long paidTickets = ticketRepo.countByStatus(Ticket.TicketStatus.PAID);
        long cancelledTickets = ticketRepo.countByStatus(Ticket.TicketStatus.CANCELLED);

        Double revenue = ticketRepo.calculateTotalRevenue(Ticket.TicketStatus.PAID);
        double totalRevenue = (revenue != null) ? revenue : 0.0;

        model.addAttribute("totalTickets", totalTickets);
        model.addAttribute("pendingTickets", pendingTickets);
        model.addAttribute("paidTickets", paidTickets);
        model.addAttribute("cancelledTickets", cancelledTickets);
        model.addAttribute("totalRevenue", totalRevenue);

        return "admin/report";
    }
}