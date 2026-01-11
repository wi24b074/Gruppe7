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

    public void printNetworkStatus() {
        for (ChargingPoint cp : points) {
            System.out.println(
                    "Ladepunkt " + cp.getPointId() +
                            " @ " + cp.getLocation().getName() +
                            " | Modus: " + cp.getMode() +
                            " | Status: " + cp.getStatus()
            );
        }
    }

    public ChargingPoint selectFreeChargingPoint(String pointId) {
        ChargingPoint cp = findById(pointId);
        if (cp == null) {
            throw new IllegalArgumentException("Ladepunkt " + pointId + " existiert nicht");
        }
        if (cp.getStatus() != ChargingStatus.IN_BETRIEB_FREI) {
            throw new IllegalStateException("Ladepunkt " + pointId + " ist nicht verfügbar");
        }
        return cp;
    }

    public void updateChargingPoint(String pointId, ChargingMode newMode, Location newLocation) {
        ChargingPoint cp = findById(pointId);
        if (cp == null) {
            throw new IllegalArgumentException("Ladepunkt " + pointId + " existiert nicht");
        }

        if (newMode != null) {
            cp.setMode(newMode);      // falls du setMode hast, sonst weglassen
        }
        if (newLocation != null) {
            cp.setLocation(newLocation); // falls du setLocation hast, sonst locationId ändern
        }
    }



    public void deactivateChargingPoint(String pointId) {
        ChargingPoint cp = findById(pointId);
        if (cp == null) {
            throw new IllegalArgumentException("Ladepunkt " + pointId + " existiert nicht");
        }
        cp.setStatus(ChargingStatus.AUSSER_BETRIEB);
    }

    public void activateChargingPoint(String pointId) {
        ChargingPoint cp = findById(pointId);
        if (cp == null) {
            throw new IllegalArgumentException("Ladepunkt " + pointId + " existiert nicht");
        }
        cp.setStatus(ChargingStatus.IN_BETRIEB_FREI);
    }

    public void clear() {
        points.clear();
    }

}
