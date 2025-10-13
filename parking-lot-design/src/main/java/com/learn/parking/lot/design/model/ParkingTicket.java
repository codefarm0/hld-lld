package com.learn.parking.lot.design.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ParkingTicket {
    private String ticketId;
    private Vehicle vehicle;
    private ParkingSpot assignedSpot;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double amountPaid;
    private TicketStatus status;
    
    public ParkingTicket(String ticketId, Vehicle vehicle, ParkingSpot assignedSpot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.assignedSpot = assignedSpot;
        this.entryTime = LocalDateTime.now();
        this.status = TicketStatus.ACTIVE;
        this.amountPaid = 0.0;
    }
    
    public long getHoursParked() {
        LocalDateTime endTime = exitTime != null ? exitTime : LocalDateTime.now();
        return ChronoUnit.HOURS.between(entryTime, endTime);
    }
    
    public void completeTicket(double amountPaid) {
        this.exitTime = LocalDateTime.now();
        this.amountPaid = amountPaid;
        this.status = TicketStatus.COMPLETED;
    }
    
    // Getters
    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getAssignedSpot() { return assignedSpot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getAmountPaid() { return amountPaid; }
    public TicketStatus getStatus() { return status; }
    
    @Override
    public String toString() {
        return "Ticket " + ticketId + " - " + vehicle + " in " + assignedSpot.getSpotId() + 
               " (Entry: " + entryTime + ", Status: " + status + ")";
    }
}

enum TicketStatus {
    ACTIVE, COMPLETED
}

