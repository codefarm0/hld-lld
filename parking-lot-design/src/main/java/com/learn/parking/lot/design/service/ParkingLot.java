package com.learn.parking.lot.design.service;

import com.learn.parking.lot.design.model.*;
import com.learn.parking.lot.design.pricing.PricingStrategy;
import com.learn.parking.lot.design.pricing.HourlyPricingStrategy;
import com.learn.parking.lot.design.payment.PaymentMethod;
import com.learn.parking.lot.design.payment.PaymentProcessorFactory;
import com.learn.parking.lot.design.payment.PaymentProcessor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLot {
    private static ParkingLot instance;
    private List<Floor> floors;
    private Map<String, ParkingTicket> activeTickets;
    private PricingStrategy pricingStrategy;
    
    private ParkingLot() {
        this.floors = new ArrayList<>();
        this.activeTickets = new ConcurrentHashMap<>();
        this.pricingStrategy = new HourlyPricingStrategy();
        initializeParkingLot();
    }
    
    public static synchronized ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }
    
    private void initializeParkingLot() {
        // Create 3 floors with different spot distributions
        for (int floorNum = 1; floorNum <= 3; floorNum++) {
            Floor floor = new Floor(floorNum);
            
            // Add spots based on the design document distribution
            addSpotsToFloor(floor, SpotType.COMPACT, 33);    // ~20% of 165 spots per floor
            addSpotsToFloor(floor, SpotType.REGULAR, 116);   // ~70% of 165 spots per floor
            addSpotsToFloor(floor, SpotType.LARGE, 10);      // ~6% of 165 spots per floor
            addSpotsToFloor(floor, SpotType.HANDICAPPED, 6); // ~4% of 165 spots per floor
            
            floors.add(floor);
        }
    }
    
    private void addSpotsToFloor(Floor floor, SpotType type, int count) {
        for (int i = 1; i <= count; i++) {
            String spotId = "F" + floor.getFloorNumber() + "-" + type.name().charAt(0) + String.format("%03d", i);
            ParkingSpot spot = new ParkingSpot(spotId, type, floor.getFloorNumber());
            floor.addSpot(spot);
        }
    }
    
    public ParkingTicket issueTicket(Vehicle vehicle) {
        if (isFull(vehicle.getRequiredSpotType())) {
            throw new RuntimeException("Parking lot is full for " + vehicle.getRequiredSpotType() + " spots");
        }
        
        ParkingSpot spot = findAndAssignSpot(vehicle);
        if (spot == null) {
            throw new RuntimeException("No available spot found for " + vehicle);
        }
        
        String ticketId = generateTicketId();
        ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, spot);
        activeTickets.put(ticketId, ticket);
        
        return ticket;
    }
    
    public Receipt processExit(String ticketId, PaymentMethod paymentMethod) {
        ParkingTicket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Invalid ticket ID: " + ticketId);
        }
        
        double fee = pricingStrategy.calculatePrice(ticket.getVehicle().getType(), ticket.getHoursParked());
        
        PaymentProcessor processor = PaymentProcessorFactory.getProcessor(paymentMethod);
        boolean paymentSuccess = processor.processPayment(paymentMethod, fee);
        
        if (!paymentSuccess) {
            throw new RuntimeException("Payment failed for ticket: " + ticketId);
        }
        
        ticket.getAssignedSpot().removeVehicle();
        ticket.completeTicket(fee);
        activeTickets.remove(ticketId);
        
        return new Receipt(ticket, fee);
    }
    
    private ParkingSpot findAndAssignSpot(Vehicle vehicle) {
        for (Floor floor : floors) {
            ParkingSpot spot = floor.findAndAssignSpot(vehicle);
            if (spot != null) {
                return spot;
            }
        }
        return null;
    }
    
    private boolean isFull(SpotType requiredType) {
        for (Floor floor : floors) {
            if (floor.getAvailableSpotCount(requiredType) > 0) {
                return false;
            }
        }
        return true;
    }
    
    private String generateTicketId() {
        return "T" + System.currentTimeMillis() + "-" + (activeTickets.size() + 1);
    }
    
    public Map<SpotType, Integer> getAvailabilitySummary() {
        Map<SpotType, Integer> summary = new HashMap<>();
        for (SpotType type : SpotType.values()) {
            int available = 0;
            for (Floor floor : floors) {
                available += floor.getAvailableSpotCount(type);
            }
            summary.put(type, available);
        }
        return summary;
    }
    
    public List<ParkingTicket> getActiveTickets() {
        return new ArrayList<>(activeTickets.values());
    }
    
    // Getters
    public List<Floor> getFloors() { return floors; }
    public PricingStrategy getPricingStrategy() { return pricingStrategy; }
}
