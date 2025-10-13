package com.learn.parking.lot.design.pricing;

import com.learn.parking.lot.design.model.VehicleType;

public interface PricingStrategy {
    double calculatePrice(VehicleType vehicleType, long hoursParked);
}

