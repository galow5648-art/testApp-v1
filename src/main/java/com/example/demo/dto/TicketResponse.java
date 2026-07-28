package com.example.demo.dto;

import com.example.demo.entity.Ticket;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TicketResponse {
    private Long id;
    private String seatNumber;
    private BigDecimal price;
    private String status;
    private String flightNumber;
    private String departure;
    private String destination;

    public static TicketResponse from(Ticket t) {
        TicketResponse r = new TicketResponse();
        r.setId(t.getId());
        r.setSeatNumber(t.getSeatNumber());
        r.setPrice(t.getPrice());
        r.setStatus(t.getStatus().name());
        r.setFlightNumber(t.getFlight().getFlightNumber());
        r.setDeparture(t.getFlight().getDeparture());
        r.setDestination(t.getFlight().getDestination());
        return r;
    }
}