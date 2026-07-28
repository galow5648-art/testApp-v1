package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "requests")
public class TicketRequest extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "ticket_id")
    Ticket ticket;

    String mealPref;
    String seatPref;
}