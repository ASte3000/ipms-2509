package com.iprody.payment.service.app.controller;

import com.iprody.payment.service.app.model.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping
public class PaymentController {
    private static final Map<Long, Payment> map = Stream.of(
                    new Payment(1, 1511.11),
                    new Payment(2, 2522.22),
                    new Payment(3, 3533.33),
                    new Payment(4, 4544.44),
                    new Payment(5, 5555.55))
            .collect(Collectors.toMap(Payment::id, payment -> payment));

    @GetMapping("/payments")
    public List<Payment> getPayment() {
        return map.values().stream().toList();
    }

    @GetMapping("/payments/{id}")
    public Payment getPayment(@PathVariable("id") long id) {
        Payment result = map.get(id);

        if (result != null) {
            return result;
        } else {
            throw new ResourceNotFoundException("Payment not found: %d".formatted(id));
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}