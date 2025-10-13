package com.learn.parking.lot.design.model;

import java.time.format.DateTimeFormatter;

public class Receipt {
    private ParkingTicket ticket;
    private double amountPaid;
    private String message;
    
    public Receipt(ParkingTicket ticket, double amountPaid) {
        this.ticket = ticket;
        this.amountPaid = amountPaid;
        this.message = "Payment successful";
    }
    
    public Receipt(ParkingTicket ticket, double amountPaid, String message) {
        this.ticket = ticket;
        this.amountPaid = amountPaid;
        this.message = message;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format(
            "=== PARKING RECEIPT ===\n" +
            "Ticket ID: %s\n" +
            "Vehicle: %s\n" +
            "Spot: %s\n" +
            "Entry Time: %s\n" +
            "Exit Time: %s\n" +
            "Hours Parked: %d\n" +
            "Amount Paid: $%.2f\n" +
            "Status: %s\n" +
            "======================",
            ticket.getTicketId(),
            ticket.getVehicle(),
            ticket.getAssignedSpot().getSpotId(),
            ticket.getEntryTime().format(formatter),
            ticket.getExitTime().format(formatter),
            ticket.getHoursParked(),
            amountPaid,
            message
        );
    }
    
    // Getters
    public ParkingTicket getTicket() { return ticket; }
    public double getAmountPaid() { return amountPaid; }
    public String getMessage() { return message; }
}

