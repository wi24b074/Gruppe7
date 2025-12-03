package org.example;

import java.util.ArrayList;
import java.util.List;

public class ChargingPointManager {
    private static final ChargingPointManager INSTANCE = new ChargingPointManager();
    private final List<ChargingPoint> points = new ArrayList<>();
    private final List<ChargingSession> sessions = new ArrayList<>();

    private ChargingPointManager(){}

    public static ChargingPointManager getInstance(){ return INSTANCE; }

    public ChargingPoint createChargingPoint(String id, ChargingMode mode, Location location){
        ChargingPoint cp = new ChargingPoint(id, mode, location);
        points.add(cp);
        location.addChargingPoint(cp);
        return cp;
    }

    public ChargingPoint findById(String id){
        return points.stream().filter(p -> p.getPointId().equals(id)).findFirst().orElse(null);
    }

    public void setStatus(String id, ChargingStatus status){
        ChargingPoint p = findById(id);
        if(p != null) p.setStatus(status);
    }

    public void addSession(ChargingSession s) {
        sessions.add(s);
    }

    public List<ChargingSession> getSessionsForCustomer(Customer customer) {
        return sessions.stream()
                .filter(s -> s.getCustomer().equals(customer))
                .toList();
    }
}
