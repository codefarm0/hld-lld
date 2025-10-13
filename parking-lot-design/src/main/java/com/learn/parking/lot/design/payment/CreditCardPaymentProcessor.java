package com.learn.parking.lot.design.payment;

public class CreditCardPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(PaymentMethod method, double amount) {
        if (method instanceof CreditCardPayment) {
            // Simulate credit card processing
            CreditCardPayment cardPayment = (CreditCardPayment) method;
            // In real implementation, this would call a payment gateway
            return cardPayment.getCardNumber().length() >= 16 && 
                   cardPayment.getCvv().length() == 3;
        }
        return false;
    }
}

