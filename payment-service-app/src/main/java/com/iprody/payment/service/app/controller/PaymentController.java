package com.iprody.payment.service.app.controller;

import com.iprody.payment.service.app.persistence.entity.Payment;
import com.iprody.payment.service.app.persistence.repostory.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentRepository paymentRepository;

    @GetMapping("/payments")
    public List<Payment> getPayment() {
        return paymentRepository.findAll();
    }

    @GetMapping("/payments/{guid}")
    public ResponseEntity<Payment> getPayment(@PathVariable("guid") String guid) {
        return ResponseEntity.of(paymentRepository.findById(UUID.fromString(guid)));
    }
}