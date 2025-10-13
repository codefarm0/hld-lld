package com.learn.parking.lot.design.model;

public class Motorcycle extends Vehicle {
    public Motorcycle(String licensePlate) {
        super(licensePlate, VehicleType.MOTORCYCLE);
    }
    
    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.COMPACT;
    }
}

