package com.learn.parking.lot.design.payment;

public class PaymentProcessorFactory {
    public static PaymentProcessor getProcessor(PaymentMethod method) {
        if (method instanceof CashPayment) {
            return new CashPaymentProcessor();
        } else if (method instanceof CreditCardPayment) {
            return new CreditCardPaymentProcessor();
        }
        throw new IllegalArgumentException("Unsupported payment method: " + method.getClass());
    }
}

