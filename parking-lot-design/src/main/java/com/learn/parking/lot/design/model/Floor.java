package com.learn.parking.lot.design.model;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Floor {
    private int floorNumber;
    private Map<SpotType, List<ParkingSpot>> spotsByType;
    private Map<SpotType, ReadWriteLock> spotTypeLocks;
    
    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spotsByType = new HashMap<>();
        this.spotTypeLocks = new HashMap<>();
        
        // Initialize spot types
        for (SpotType type : SpotType.values()) {
            spotsByType.put(type, new ArrayList<>());
            spotTypeLocks.put(type, new ReentrantReadWriteLock());
        }
    }
    
    public void addSpot(ParkingSpot spot) {
        spotsByType.get(spot.getType()).add(spot);
    }
    
    public ParkingSpot findAndAssignSpot(Vehicle vehicle) {
        SpotType requiredType = vehicle.getRequiredSpotType();
        ReadWriteLock lock = spotTypeLocks.get(requiredType);
        
        lock.writeLock().lock();
        try {
            // Try exact match first
            ParkingSpot spot = findAvailableSpotOfType(requiredType);
            if (spot != null && spot.assignVehicle(vehicle)) {
                return spot;
            }
            
            // Try larger spots
            for (SpotType type : SpotType.values()) {
                if (type != requiredType && spot.canAccommodate(requiredType)) {
                    spot = findAvailableSpotOfType(type);
                    if (spot != null && spot.assignVehicle(vehicle)) {
                        return spot;
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
        
        return null;
    }
    
    private ParkingSpot findAvailableSpotOfType(SpotType type) {
        for (ParkingSpot spot : spotsByType.get(type)) {
            if (!spot.isOccupied()) {
                return spot;
            }
        }
        return null;
    }
    
    public int getAvailableSpotCount(SpotType type) {
        ReadWriteLock lock = spotTypeLocks.get(type);
        lock.readLock().lock();
        try {
            return (int) spotsByType.get(type).stream()
                    .filter(spot -> !spot.isOccupied())
                    .count();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public int getTotalSpotCount(SpotType type) {
        return spotsByType.get(type).size();
    }
    
    public List<ParkingSpot> getAllSpots() {
        List<ParkingSpot> allSpots = new ArrayList<>();
        for (List<ParkingSpot> spots : spotsByType.values()) {
            allSpots.addAll(spots);
        }
        return allSpots;
    }
    
    // Getters
    public int getFloorNumber() { return floorNumber; }
    public Map<SpotType, List<ParkingSpot>> getSpotsByType() { return spotsByType; }
}

