package org.example;

import java.math.BigDecimal;

public class Tariff {

    private final String tariffId;
    private final Location location;

    private BigDecimal acPricePerKWh = BigDecimal.ZERO;
    private BigDecimal dcPricePerKWh = BigDecimal.ZERO;

    public Tariff(String tariffId, Location location) {
        this.tariffId = tariffId;
        this.location = location;
    }

    public void setPrices(BigDecimal acPrice, BigDecimal dcPrice) {
        this.acPricePerKWh = acPrice;
        this.dcPricePerKWh = dcPrice;
    }

    public BigDecimal getAcPricePerKWh() {
        return acPricePerKWh;
    }

    public BigDecimal getDcPricePerKWh() {
        return dcPricePerKWh;
    }

    public BigDecimal calculateCost(ChargingMode mode, double kWh) {
        BigDecimal energy = BigDecimal.valueOf(kWh);

        if (mode == ChargingMode.AC) {
            return acPricePerKWh.multiply(energy);
        } else {
            return dcPricePerKWh.multiply(energy);
        }
    }

    public String getTariffId() {
        return tariffId;
    }

    public Location getLocation() {
        return location;
    }
}
