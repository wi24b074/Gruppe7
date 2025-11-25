package org.example;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private final String locationId;
    private String name;
    private String address;
    private final List<ChargingPoint> chargingPoints = new ArrayList<>();
    private Tariff tariff;

    public Location(String locationId, String name, String address){
        this.locationId = locationId;
        this.name = name;
        this.address = address;
    }

    public String getLocationId(){ return locationId; }
    public String getName(){ return name; }
    public Location setName(String name){ this.name = name; return this; }
    public String getAddress(){ return address; }
    public Location setAddress(String address){ this.address = address; return this; }
    public Tariff getTariff() {

        return tariff;
    }
    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }
    public void addChargingPoint(ChargingPoint cp){
        chargingPoints.add(cp);
    }

    public List<ChargingPoint> getChargingPoints(){ return List.copyOf(chargingPoints); }
}

