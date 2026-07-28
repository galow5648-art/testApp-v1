package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    Flight flight;

    String seatNumber;
    BigDecimal price;

    @Enumerated(EnumType.STRING)
    Status status = Status.AVAILABLE;

    public enum Status {
        AVAILABLE, RESERVED, PAID
    }
}