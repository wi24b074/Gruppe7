package org.example;

import org.example.CustomerManager;
import org.example.BillingManager;
import org.example.ChargingPointManager;
import org.example.LocationManager;
import org.example.PricingManager;
import java.math.BigDecimal;

public class MainProgram {
    public static void main(String[] args){
        System.out.println("Demo: Elektrotankstellennetz Domain");

        LocationManager lm = LocationManager.getInstance();
        ChargingPointManager cpm = ChargingPointManager.getInstance();
        CustomerManager cm = CustomerManager.getInstance();


        Location loc = lm.findById("LOC-1");
        if (loc == null) {
            loc = lm.createLocation("LOC-1", "Hauptbahnhof", "Bahnhofstrasse 1");
        }
        System.out.println("Location vorhanden: " + loc.getLocationId() + " - " + loc.getName());

        ChargingPoint cp = cpm.findById("CP-1");
        if (cp == null) {
            cp = cpm.createChargingPoint("CP-1", ChargingMode.AC, loc);
        }
        System.out.println("ChargingPoint vorhanden: " + cp.getPointId() + " (Mode: " + cp.getMode() + ")");


        System.out.println("Aktueller Status von CP-1: " + cp.getStatus());

        cpm.setStatus("CP-1", ChargingStatus.AUSSER_BETRIEB);
        ChargingPoint updated = cpm.findById("CP-1");
        System.out.println("Neuer Status von CP-1: " + updated.getStatus());


        Customer cust = cm.registerCustomer("max@example.com", "Max Mustermann");
        System.out.println("Registered customer: " + cust.getEmail());

        cust.getAccount().credit(BigDecimal.valueOf(10.00));
        System.out.println("Customer balance after topup: " + cust.getAccount().getBalance());
    }
}
