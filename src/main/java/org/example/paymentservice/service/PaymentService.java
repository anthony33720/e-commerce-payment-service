package org.example.paymentservice.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.net.RequestOptions;
import org.example.paymentservice.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public Payment createCharge(String token, Double amount, String currency, String description) throws StripeException {
        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(stripeApiKey)
                .build();

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int)(amount * 100)); // amount is in cents
        chargeParams.put("currency", currency);
        chargeParams.put("source", token);
        chargeParams.put("description", description);

        Charge charge = Charge.create(chargeParams, requestOptions);


        Payment payment = new Payment();
        payment.setPaymentId(charge.getId());
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setDescription(description);
        payment.setPaymentDate(charge.getCreated());


        return payment;
    }


    public String createCustomer(String email, String source) throws StripeException {
        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(stripeApiKey)
                .build();

        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", email);
        customerParams.put("source", source);

        Customer customer = Customer.create(customerParams, requestOptions);
        return customer.toJson();
    }

}
