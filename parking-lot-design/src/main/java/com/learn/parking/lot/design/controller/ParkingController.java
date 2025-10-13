package com.learn.parking.lot.design.controller;

import com.learn.parking.lot.design.model.*;
import com.learn.parking.lot.design.service.ParkingLot;
import com.learn.parking.lot.design.payment.CashPayment;
import com.learn.parking.lot.design.payment.CreditCardPayment;
import com.learn.parking.lot.design.payment.PaymentMethod;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {
    
    private final ParkingLot parkingLot = ParkingLot.getInstance();
    
    @PostMapping("/entry")
    public Map<String, Object> vehicleEntry(@RequestBody VehicleEntryRequest request) {
        try {
            Vehicle vehicle = createVehicle(request.getVehicleType(), request.getLicensePlate());
            ParkingTicket ticket = parkingLot.issueTicket(vehicle);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("ticket", ticket);
            response.put("message", "Vehicle parked successfully");
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        }
    }
    
    @PostMapping("/exit")
    public Map<String, Object> vehicleExit(@RequestBody VehicleExitRequest request) {
        try {
            PaymentMethod paymentMethod = createPaymentMethod(request.getPaymentMethod(), request.getPaymentDetails());
            Receipt receipt = parkingLot.processExit(request.getTicketId(), paymentMethod);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("receipt", receipt);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        }
    }
    
    @GetMapping("/availability")
    public Map<String, Object> getAvailability() {
        Map<SpotType, Integer> availability = parkingLot.getAvailabilitySummary();
        List<ParkingTicket> activeTickets = parkingLot.getActiveTickets();
        
        Map<String, Object> response = new HashMap<>();
        response.put("availability", availability);
        response.put("activeTickets", activeTickets.size());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @GetMapping("/status")
    public Map<String, Object> getParkingLotStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("floors", parkingLot.getFloors().size());
        status.put("availability", parkingLot.getAvailabilitySummary());
        status.put("activeTickets", parkingLot.getActiveTickets());
        return status;
    }
    
    private Vehicle createVehicle(String vehicleType, String licensePlate) {
        switch (vehicleType.toUpperCase()) {
            case "MOTORCYCLE":
                return new Motorcycle(licensePlate);
            case "CAR":
                return new Car(licensePlate);
            case "TRUCK":
                return new Truck(licensePlate);
            default:
                throw new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
        }
    }
    
    private PaymentMethod createPaymentMethod(String method, Map<String, String> details) {
        switch (method.toUpperCase()) {
            case "CASH":
                double amount = Double.parseDouble(details.get("amount"));
                return new CashPayment(amount);
            case "CREDIT_CARD":
                return new CreditCardPayment(
                    details.get("cardNumber"),
                    details.get("cvv"),
                    details.get("expiryDate")
                );
            default:
                throw new IllegalArgumentException("Invalid payment method: " + method);
        }
    }
    
    // Request DTOs
    public static class VehicleEntryRequest {
        private String vehicleType;
        private String licensePlate;
        
        // Getters and setters
        public String getVehicleType() { return vehicleType; }
        public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
        public String getLicensePlate() { return licensePlate; }
        public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    }
    
    public static class VehicleExitRequest {
        private String ticketId;
        private String paymentMethod;
        private Map<String, String> paymentDetails;
        
        // Getters and setters
        public String getTicketId() { return ticketId; }
        public void setTicketId(String ticketId) { this.ticketId = ticketId; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public Map<String, String> getPaymentDetails() { return paymentDetails; }
        public void setPaymentDetails(Map<String, String> paymentDetails) { this.paymentDetails = paymentDetails; }
    }
}

