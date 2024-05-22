package org.example.paymentservice.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import org.example.paymentservice.Payment;
import org.example.paymentservice.exception.PaymentException;
import org.example.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createCharge(String token, Double amount, String currency, String description) throws StripeException {
        try {
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int)(amount * 100)); // Stripe expects amount in cents
            chargeParams.put("currency", currency);
            chargeParams.put("source", token);
            chargeParams.put("description", description);

            Charge charge = Charge.create(chargeParams);

            Payment payment = new Payment();
            payment.setPaymentId(charge.getId());
            payment.setAmount(amount);
            payment.setCurrency(currency);
            payment.setPaymentDate(LocalDateTime.now());

            return paymentRepository.save(payment);
        } catch (StripeException e) {
            throw new PaymentException("Failed to create charge: " + e.getMessage());
        }
    }

    public Customer createCustomer(String email, String source) throws StripeException {
        try {
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("email", email);
            customerParams.put("source", source); // e.g., tok_visa

            return Customer.create(customerParams);
        } catch (StripeException e) {
            throw new PaymentException("Failed to create customer: " + e.getMessage());
        }
    }
}

