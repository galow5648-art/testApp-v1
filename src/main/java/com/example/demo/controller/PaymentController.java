package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.PayRequest;
import com.example.demo.service.PaymentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    @PreAuthorize("hasRole('USER')")
    public BaseResponse pay(@RequestBody PayRequest payRequest) {
        paymentService.payForTicket(payRequest.getTicketId());
        return BaseResponse.ok("Оплата прошла успешно");
    }
}