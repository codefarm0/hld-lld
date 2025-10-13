package com.learn.parking.lot.design.pricing;

import com.learn.parking.lot.design.model.VehicleType;
import java.util.HashMap;
import java.util.Map;

public class HourlyPricingStrategy implements PricingStrategy {
    private Map<VehicleType, Double> hourlyRates;
    
    public HourlyPricingStrategy() {
        this.hourlyRates = new HashMap<>();
        hourlyRates.put(VehicleType.MOTORCYCLE, 2.0);
        hourlyRates.put(VehicleType.CAR, 5.0);
        hourlyRates.put(VehicleType.TRUCK, 10.0);
    }
    
    @Override
    public double calculatePrice(VehicleType vehicleType, long hoursParked) {
        if (hoursParked == 0) hoursParked = 1; // Minimum 1 hour
        
        double baseRate = hourlyRates.get(vehicleType);
        double totalPrice = baseRate * hoursParked;
        
        // 20% discount for 24+ hours
        if (hoursParked >= 24) {
            totalPrice *= 0.8;
        }
        
        return totalPrice;
    }
}

