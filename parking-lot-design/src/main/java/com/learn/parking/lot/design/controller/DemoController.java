package com.learn.parking.lot.design.controller;

import com.learn.parking.lot.design.model.*;
import com.learn.parking.lot.design.service.ParkingLot;
import com.learn.parking.lot.design.payment.CashPayment;
import com.learn.parking.lot.design.payment.CreditCardPayment;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    
    private final ParkingLot parkingLot = ParkingLot.getInstance();
    
    @GetMapping("/scenario1")
    public Map<String, Object> scenario1_CarEntryAndExit() {
        Map<String, Object> result = new HashMap<>();
        List<String> steps = new ArrayList<>();
        
        try {
            // Step 1: Car enters
            Car car = new Car("ABC-1234");
            ParkingTicket ticket = parkingLot.issueTicket(car);
            steps.add("✓ Car " + car.getLicensePlate() + " entered and got ticket " + ticket.getTicketId());
            
            // Step 2: Check availability
            Map<SpotType, Integer> availability = parkingLot.getAvailabilitySummary();
            steps.add("✓ Current availability: " + availability);
            
            // Step 3: Car exits with cash payment
            Thread.sleep(1000); // Simulate parking time
            CashPayment cashPayment = new CashPayment(20.0);
            Receipt receipt = parkingLot.processExit(ticket.getTicketId(), cashPayment);
            steps.add("✓ Car exited successfully. Receipt: " + receipt.toString());
            
            result.put("success", true);
            result.put("steps", steps);
            result.put("finalAvailability", parkingLot.getAvailabilitySummary());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("steps", steps);
        }
        
        return result;
    }
    
    @GetMapping("/scenario2")
    public Map<String, Object> scenario2_MultipleVehicles() {
        Map<String, Object> result = new HashMap<>();
        List<String> steps = new ArrayList<>();
        List<ParkingTicket> tickets = new ArrayList<>();
        
        try {
            // Multiple vehicles enter
            Vehicle[] vehicles = {
                new Car("CAR-001"),
                new Motorcycle("BIKE-001"),
                new Truck("TRUCK-001"),
                new Car("CAR-002")
            };
            
            for (Vehicle vehicle : vehicles) {
                ParkingTicket ticket = parkingLot.issueTicket(vehicle);
                tickets.add(ticket);
                steps.add("✓ " + vehicle + " entered and got ticket " + ticket.getTicketId());
            }
            
            // Check availability
            Map<SpotType, Integer> availability = parkingLot.getAvailabilitySummary();
            steps.add("✓ Current availability: " + availability);
            
            // Some vehicles exit
            Thread.sleep(2000); // Simulate parking time
            
            // Car 1 exits with credit card
            CreditCardPayment cardPayment = new CreditCardPayment("1234567890123456", "123", "12/25");
            Receipt receipt1 = parkingLot.processExit(tickets.get(0).getTicketId(), cardPayment);
            steps.add("✓ " + tickets.get(0).getVehicle() + " exited. Receipt: $" + receipt1.getAmountPaid());
            
            // Motorcycle exits with cash
            CashPayment cashPayment = new CashPayment(10.0);
            Receipt receipt2 = parkingLot.processExit(tickets.get(1).getTicketId(), cashPayment);
            steps.add("✓ " + tickets.get(1).getVehicle() + " exited. Receipt: $" + receipt2.getAmountPaid());
            
            result.put("success", true);
            result.put("steps", steps);
            result.put("finalAvailability", parkingLot.getAvailabilitySummary());
            result.put("remainingTickets", parkingLot.getActiveTickets().size());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("steps", steps);
        }
        
        return result;
    }
    
    @GetMapping("/scenario3")
    public Map<String, Object> scenario3_ConcurrentEntries() {
        Map<String, Object> result = new HashMap<>();
        List<String> steps = new ArrayList<>();
        
        try {
            // Simulate concurrent entries
            List<Thread> threads = new ArrayList<>();
            List<ParkingTicket> concurrentTickets = Collections.synchronizedList(new ArrayList<>());
            
            for (int i = 0; i < 5; i++) {
                final int index = i;
                Thread thread = new Thread(() -> {
                    try {
                        Car car = new Car("CONCURRENT-" + String.format("%03d", index));
                        ParkingTicket ticket = parkingLot.issueTicket(car);
                        concurrentTickets.add(ticket);
                        steps.add("✓ Concurrent entry: " + car + " got ticket " + ticket.getTicketId());
                    } catch (Exception e) {
                        steps.add("✗ Concurrent entry failed: " + e.getMessage());
                    }
                });
                threads.add(thread);
                thread.start();
            }
            
            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }
            
            steps.add("✓ All concurrent entries completed. Total tickets: " + concurrentTickets.size());
            
            // Check final availability
            Map<SpotType, Integer> availability = parkingLot.getAvailabilitySummary();
            steps.add("✓ Final availability: " + availability);
            
            result.put("success", true);
            result.put("steps", steps);
            result.put("concurrentTickets", concurrentTickets.size());
            result.put("finalAvailability", availability);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("steps", steps);
        }
        
        return result;
    }
    
    @GetMapping("/reset")
    public Map<String, Object> resetParkingLot() {
        // This would reset the parking lot in a real implementation
        // For demo purposes, we'll just return current status
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Parking lot reset (demo mode)");
        result.put("currentStatus", parkingLot.getAvailabilitySummary());
        result.put("activeTickets", parkingLot.getActiveTickets().size());
        return result;
    }
}

