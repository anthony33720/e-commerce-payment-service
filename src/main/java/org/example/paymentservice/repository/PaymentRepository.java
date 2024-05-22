package org.example.paymentservice.repository;

import org.example.paymentservice.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
