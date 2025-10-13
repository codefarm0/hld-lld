package com.learn.parking.lot.design.payment;

public class CashPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(PaymentMethod method, double amount) {
        if (method instanceof CashPayment) {
            CashPayment cashPayment = (CashPayment) method;
            return cashPayment.getAmountGiven() >= amount;
        }
        return false;
    }
}

