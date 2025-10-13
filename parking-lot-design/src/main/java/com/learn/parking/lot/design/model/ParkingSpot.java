package com.learn.parking.lot.design.model;

import java.time.LocalDateTime;

public class ParkingSpot {
    private String spotId;
    private SpotType type;
    private boolean isOccupied;
    private Vehicle parkedVehicle;
    private LocalDateTime occupiedAt;
    private int floorNumber;
    
    public ParkingSpot(String spotId, SpotType type, int floorNumber) {
        this.spotId = spotId;
        this.type = type;
        this.floorNumber = floorNumber;
        this.isOccupied = false;
    }
    
    public synchronized boolean assignVehicle(Vehicle vehicle) {
        if (isOccupied) {
            return false;
        }
        this.isOccupied = true;
        this.parkedVehicle = vehicle;
        this.occupiedAt = LocalDateTime.now();
        return true;
    }
    
    public synchronized void removeVehicle() {
        this.isOccupied = false;
        this.parkedVehicle = null;
        this.occupiedAt = null;
    }
    
    public boolean canAccommodate(SpotType requiredType) {
        // A vehicle can park in a spot of its required type or larger
        return !isOccupied && (type == requiredType || 
               (requiredType == SpotType.COMPACT && type == SpotType.REGULAR) ||
               (requiredType == SpotType.REGULAR && type == SpotType.LARGE) ||
               (requiredType == SpotType.COMPACT && type == SpotType.LARGE));
    }
    
    // Getters
    public String getSpotId() { return spotId; }
    public SpotType getType() { return type; }
    public boolean isOccupied() { return isOccupied; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }
    public LocalDateTime getOccupiedAt() { return occupiedAt; }
    public int getFloorNumber() { return floorNumber; }
    
    @Override
    public String toString() {
        return "Spot " + spotId + " (" + type + ", Floor " + floorNumber + ") - " + 
               (isOccupied ? "Occupied by " + parkedVehicle : "Available");
    }
}

