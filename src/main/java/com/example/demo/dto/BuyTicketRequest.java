package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BuyTicketRequest {
    private Long flightId;
    private String seatNumber;
    private BigDecimal price;
}