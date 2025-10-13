package com.learn.parking.lot.design.payment;

public interface PaymentProcessor {
    boolean processPayment(PaymentMethod method, double amount);
}

