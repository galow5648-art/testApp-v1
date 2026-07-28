package com.example.demo.service;

import com.example.demo.dto.TicketResponse;
import com.example.demo.entity.Flight;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.repository.FlightRepository;
import com.example.demo.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;

    public TicketService(TicketRepository ticketRepository, FlightRepository flightRepository) {
        this.ticketRepository = ticketRepository;
        this.flightRepository = flightRepository;
    }

    public Ticket buyTicket(User user, Long flightId, String seatNumber, BigDecimal price) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Рейс не найден"));

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setSeatNumber(seatNumber);
        ticket.setPrice(price);
        ticket.setStatus(Ticket.Status.RESERVED);

        return ticketRepository.save(ticket);
    }

    public List<TicketResponse> getMyTickets(Long userId) {
        List<Ticket> tickets = ticketRepository.findAllByUserWithFlight(userId);
        List<TicketResponse> result = new ArrayList<>();

        for (Ticket t : tickets) {
            TicketResponse response = new TicketResponse();
            response.setId(t.getId());
            response.setFlightNumber(t.getFlight().getFlightNumber());
            response.setDeparture(t.getFlight().getDeparture());
            response.setDestination(t.getFlight().getDestination());
            response.setSeatNumber(t.getSeatNumber());
            response.setPrice(t.getPrice());
            response.setStatus(t.getStatus().name());

            result.add(response);
        }

        return result;
    }
}