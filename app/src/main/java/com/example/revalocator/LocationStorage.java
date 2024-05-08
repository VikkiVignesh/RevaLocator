package com.example.revalocator;

import java.util.HashMap;

public class LocationStorage {
    public HashMap<String, double[]> locationMap;

    public LocationStorage() {
        locationMap = new HashMap<>();
    }

    public void addLocation(String name, double latitude, double longitude) {
        double[] coordinates = {latitude, longitude};
        locationMap.put(name, coordinates);
    }

    public double[] getLocationCoordinates(String name) {
        return locationMap.get(name);
    }

    public void removeLocation(String name) {
        locationMap.remove(name);
    }
}
