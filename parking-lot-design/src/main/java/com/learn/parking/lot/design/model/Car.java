package com.learn.parking.lot.design.model;

public class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
    
    @Override
    public SpotType getRequiredSpotType() {
        return SpotType.REGULAR;
    }
}

