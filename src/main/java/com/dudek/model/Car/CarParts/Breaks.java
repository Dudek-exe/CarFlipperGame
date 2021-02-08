package com.dudek.model.Car.CarParts;

import java.math.BigDecimal;

public final class Breaks extends CarPart {
    public Breaks() {
        super(BigDecimal.valueOf(0.1), "Hamulce", BigDecimal.valueOf(300));
    }

    Breaks(boolean isOK){
        super(BigDecimal.valueOf(0.1), "Hamulce", BigDecimal.valueOf(300),isOK);
    }
}
