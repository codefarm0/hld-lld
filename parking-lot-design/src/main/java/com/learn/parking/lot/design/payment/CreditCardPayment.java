package com.learn.parking.lot.design.payment;

public class CreditCardPayment implements PaymentMethod {
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    
    public CreditCardPayment(String cardNumber, String cvv, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }
    
    @Override
    public String getMethodName() {
        return "CREDIT_CARD";
    }
    
    // Getters
    public String getCardNumber() { return cardNumber; }
    public String getCvv() { return cvv; }
    public String getExpiryDate() { return expiryDate; }
}

