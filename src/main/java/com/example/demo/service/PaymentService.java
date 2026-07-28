package com.example.demo.service;

import com.example.demo.entity.Payment;
import com.example.demo.entity.Ticket;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;

    public PaymentService(PaymentRepository paymentRepository, TicketRepository ticketRepository) {
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
    }

    public void payForTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);

        if (ticket != null) {
            ticket.setStatus(Ticket.Status.PAID);
            ticketRepository.save(ticket);

            Payment payment = new Payment();
            payment.setTicket(ticket);
            payment.setAmount(ticket.getPrice());
            payment.setPaidAt(LocalDateTime.now());
            payment.setStatus(Payment.Status.SUCCESS);

            paymentRepository.save(payment);
        }
    }
}