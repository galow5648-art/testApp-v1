package com.example.demo.repository;

import com.example.demo.entity.TicketRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<TicketRequest, Long> {
}