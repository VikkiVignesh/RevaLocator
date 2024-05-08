package com.example.revalocator;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Timeline extends Fragment implements OnMapReadyCallback {

    private DatabaseReference mDatabase;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private double startLat = 12.974148;
    private double startLng = 77.561220;
    private List<LatLng> defaultCoordinates = new ArrayList<>();
    private Polyline polyline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("markerLocations");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Add default coordinates to the list
        defaultCoordinates.add(new LatLng(12.974470, 77.561125));
        defaultCoordinates.add(new LatLng(12.973643, 77.561067));

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check if mMap is null before using it
        if (mMap != null) {
            // Add markers for default coordinates
            for (LatLng coordinate : defaultCoordinates) {
                mMap.addMarker(new MarkerOptions().position(coordinate));
            }

            // Start location updates
            startLocationUpdates();

            // Listener to retrieve data from the database
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        // Draw polyline for the path
                        List<LatLng> pathCoordinates = new ArrayList<>();
                        pathCoordinates.add(new LatLng(startLat, startLng));

                        for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                            Double latitude = markerSnapshot.child("latitude").getValue(Double.class);
                            Double longitude = markerSnapshot.child("longitude").getValue(Double.class);

                            // Check if latitude and longitude are not null
                            if (latitude != null && longitude != null) {
                                LatLng currentCoordinate = new LatLng(latitude, longitude);
                                pathCoordinates.add(currentCoordinate);
                            }
                        }

                        drawPath(pathCoordinates);
                    } catch (Exception e) {
                        // Handle any exceptions that occur during data retrieval
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    databaseError.toException().printStackTrace();
                }
            });
        } else {
            // Handle the case where mMap is null
            Toast.makeText(getContext(), "Map is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocationUpdates() {
        // Create the location request to start receiving updates
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000) // 10 seconds
                .setFastestInterval(5000); // 5 seconds

        // Create location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        updateLocation(location);
                    }
                }
            }
        };

        // Request location updates
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void updateLocation(Location location) {
        // Check if user's location is near any of the default points
        for (LatLng coordinate : defaultCoordinates) {
            float[] distance = new float[1];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                    coordinate.latitude, coordinate.longitude, distance);
            if (distance[0] < 3) { // Adjust this value as needed for your accuracy requirements
                // Do something when user is near the coordinate
                mMap.addMarker(new MarkerOptions().position(coordinate).icon(getMarkerIcon(Color.GREEN)));
            }
        }
    }

    private void drawPath(List<LatLng> pathCoordinates) {
        try {
            // Check if mMap is null before using it
            if (mMap != null) {
                // Draw polyline for the path
                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(pathCoordinates)
                        .color(Color.RED)
                        .width(5);

                polyline = mMap.addPolyline(polylineOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pathCoordinates.get(0), 12));
            } else {
                // Handle the case where mMap is null
                Toast.makeText(getContext(), "Map is not available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during polyline drawing
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop location updates when the fragment is destroyed
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private BitmapDescriptor getMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
