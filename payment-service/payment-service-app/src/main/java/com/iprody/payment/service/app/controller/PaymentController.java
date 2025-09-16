package com.iprody.payment.service.app.controller;

import com.iprody.payment.service.app.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping
public class PaymentController {
    private static final Map<Long, Payment> paymentsMap = Stream.of(
                    new Payment(1, 1511.11),
                    new Payment(2, 2522.22),
                    new Payment(3, 3533.33),
                    new Payment(4, 4544.44)
                    new Payment(5, 5555.55))
            .collect(Collectors.toMap(Payment::id, payment -> payment));

    @GetMapping("/payments")
    public List<Payment> getPayment() {
        return paymentsMap.values().stream().toList();
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable("id") long id) {
        return ResponseEntity.ofNullable(paymentsMap.get(id));
    }
}