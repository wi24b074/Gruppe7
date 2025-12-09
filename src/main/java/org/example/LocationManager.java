package org.example;

import org.example.Location;
import java.util.ArrayList;
import java.util.List;

public class LocationManager {
    private static final LocationManager INSTANCE = new LocationManager();
    private final List<Location> locations = new ArrayList<>();

    private LocationManager(){}

    public static LocationManager getInstance(){ return INSTANCE; }

    public Location createLocation(String id, String name, String address){
        Location loc = new Location(id, name, address);
        locations.add(loc);
        return loc;
    }

    public Location findById(String id){
        return locations.stream().filter(l -> l.getLocationId().equals(id)).findFirst().orElse(null);
    }

    public List<Location> getAll(){ return List.copyOf(locations); }

    public List<Location> getAvailableLocations() {
        List<Location> result = new ArrayList<>();
        for (Location loc : locations) {
            boolean hasAvailable = loc.getChargingPoints().stream()
                    .anyMatch(cp -> cp.getStatus() == ChargingStatus.IN_BETRIEB_FREI);
            if (hasAvailable) {
                result.add(loc);
            }
        }
        return result;
    }

    public void printLocationOverview() {
        for (Location loc : getAll()) {
            int total = loc.getChargingPoints().size();
            long free = loc.getChargingPoints().stream()
                    .filter(cp -> cp.getStatus() == ChargingStatus.IN_BETRIEB_FREI)
                    .count();
            long occupied = loc.getChargingPoints().stream()
                    .filter(cp -> cp.getStatus() == ChargingStatus.IN_BETRIEB_BESETZT)
                    .count();
            long outOfOrder = loc.getChargingPoints().stream()
                    .filter(cp -> cp.getStatus() == ChargingStatus.AUSSER_BETRIEB)
                    .count();

            System.out.println(
                    loc.getLocationId() + " - " + loc.getName() +
                            " | Ladepunkte gesamt: " + total +
                            " | frei: " + free +
                            " | belegt: " + occupied +
                            " | außer Betrieb: " + outOfOrder
            );
        }
    }

    public static class LocationDetails {
        private final Location location;
        private final Tariff tariff;
        private final List<ChargingPoint> chargingPoints;

        public LocationDetails(Location location, Tariff tariff, List<ChargingPoint> chargingPoints) {
            this.location = location;
            this.tariff = tariff;
            this.chargingPoints = chargingPoints;
        }

        public Location getLocation() { return location; }
        public Tariff getTariff() { return tariff; }
        public List<ChargingPoint> getChargingPoints() { return chargingPoints; }
    }

    public LocationDetails getLocationDetails(Location locationId) {
        Location loc = findById(String.valueOf(locationId));
        if (loc == null) {
            return null;
        }

        PricingManager pm = PricingManager.getInstance();
        Tariff tariff = pm.getTariffForLocation(locationId);

        return new LocationDetails(loc, tariff, loc.getChargingPoints());
    }

}
