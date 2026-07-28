package com.example.demo.controller;

import com.example.demo.dto.BuyTicketRequest;
import com.example.demo.dto.TicketResponse;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CurrentUser;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.TicketService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public TicketController(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @PostMapping("/buy")
    @PreAuthorize("hasRole('USER')")
    public Ticket buyTicket(@CurrentUser UserPrincipal principal, @RequestBody BuyTicketRequest request) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        return ticketService.buyTicket(user, request.getFlightId(), request.getSeatNumber(), request.getPrice());
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public List<TicketResponse> getMyTickets(@CurrentUser UserPrincipal principal) {
        return ticketService.getMyTickets(principal.getId());
    }
}