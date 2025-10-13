package com.learn.parking.lot.design.payment;

public class CashPayment implements PaymentMethod {
    private double amountGiven;
    
    public CashPayment(double amountGiven) {
        this.amountGiven = amountGiven;
    }
    
    @Override
    public String getMethodName() {
        return "CASH";
    }
    
    public double getAmountGiven() { return amountGiven; }
}

