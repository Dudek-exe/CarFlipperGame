package com.dudek.model.Mechanic;

import com.dudek.model.Car.Car;
import com.dudek.model.Car.CarParts.CarPart;

import java.math.BigDecimal;

public class Marian extends Mechanic {

    protected Marian() {
        super(80, BigDecimal.valueOf(400));
    }

    @Override
    public BigDecimal repairCarPart(Car car, CarPart carPart) {
        carPart.repair(this);

        if (!carPart.isOk()) {
            System.err.println("Marian nie umie naprawic auta, musial wezwac Janusza, bedzie to dodatkowo kosztowac!");
            Janusz janusz = new Janusz();
            return janusz.repairCarPart(car,carPart).add(getMechanicSalary());
        }
        return calculateRepairCost(car, carPart);
    }

}

