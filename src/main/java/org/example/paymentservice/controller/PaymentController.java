package org.example.paymentservice.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.example.paymentservice.Payment;
import org.example.paymentservice.exception.PaymentException;
import org.example.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/charge")
    public ResponseEntity<?> createCharge(@RequestParam String token,
                                          @RequestParam Double amount,
                                          @RequestParam String currency,
                                          @RequestParam String description) {
        try {
            Payment payment = paymentService.createCharge(token, amount, currency, description);
            return ResponseEntity.ok(payment);
        } catch (PaymentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/customer")
    public ResponseEntity<?> createCustomer(@RequestParam String email, @RequestParam String source) {
        try {
            Customer customer = paymentService.createCustomer(email, source);
            return ResponseEntity.ok(customer);
        } catch (PaymentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
