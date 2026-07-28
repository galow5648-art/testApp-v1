package com.example.demo.repository;

import com.example.demo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
        SELECT t FROM Ticket t
        JOIN FETCH t.flight f
        JOIN FETCH t.user u
        WHERE u.id = :userId
        ORDER BY f.flightDate
        """)
    List<Ticket> findAllByUserWithFlight(@Param("userId") Long userId);
}