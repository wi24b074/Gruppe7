package org.example;

public class ChargingPoint {

    private final String pointId;
    private ChargingMode mode;
    private ChargingStatus status;
    private final Location location; //Location auf branch feature/location

    public ChargingPoint(String pointId, ChargingMode mode, Location location){
        this.pointId = pointId;
        this.mode = mode;
        this.location = location;
        this.status = ChargingStatus.IN_BETRIEB_FREI;
    }

    public String getPointId(){ return pointId; }
    public ChargingMode getMode(){ return mode; }
    public ChargingPoint setMode(ChargingMode mode){ this.mode = mode; return this; }
    public ChargingStatus getStatus(){ return status; }
    public void setStatus(ChargingStatus status){ this.status = status; }
    public Location getLocation(){ return location; }
}
