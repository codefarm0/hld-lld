package com.learn.parking.lot.design.model;

public class Truck extends Vehicle {
    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
    
    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.LARGE;
    }
}

