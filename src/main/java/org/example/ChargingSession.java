package org.example;

import java.math.BigDecimal;
import java.util.Date;

public class ChargingSession {

    private final String sessionId;
    private final Customer customer;
    private final ChargingPoint chargingPoint;

    private Date startTime;
    private Date endTime;

    private double energyKWh = 0.0;
    private BigDecimal totalCost = BigDecimal.ZERO;

    public ChargingSession(String sessionId, Customer customer, ChargingPoint chargingPoint) {
        this.sessionId = sessionId;
        this.customer = customer;
        this.chargingPoint = chargingPoint;
        this.startTime = new Date();
        chargingPoint.setStatus(ChargingStatus.IN_BETRIEB_BESETZT);
    }





    public void endSession(double energyKWh, Tariff tariff) {
        this.endTime = new Date();
        this.energyKWh = energyKWh;

        ChargingMode mode = chargingPoint.getMode();
        this.totalCost = tariff.calculateCost(mode, energyKWh);
    }

    public String getSessionId() {
        return sessionId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ChargingPoint getChargingPoint() {
        return chargingPoint;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public double getEnergyKWh() {
        return energyKWh;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }
}
