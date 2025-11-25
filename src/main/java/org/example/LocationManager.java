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
}
