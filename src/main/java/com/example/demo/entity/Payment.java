package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "ticket_id")
    Ticket ticket;

    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    Status status = Status.PENDING;

    LocalDateTime paidAt;

    public enum Status {
        PENDING, SUCCESS, FAILED
    }
}