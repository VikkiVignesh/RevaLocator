package com.example.revalocator;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Timeline extends Fragment implements OnMapReadyCallback {
    String srn;
    private DatabaseReference mDatabase;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private double startLat = 12.974148;
    private double startLng = 77.561220;
    private List<LatLng> defaultCoordinates = new ArrayList<>();
    private Marker lastVisitedMarker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_timeline, container, false);
        srn=getActivity().getIntent().getStringExtra("srn");


        return  rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Add default coordinates to the list
        defaultCoordinates.add(new LatLng(12.974470, 77.561125));
        defaultCoordinates.add(new LatLng(12.973643, 77.561067));
        defaultCoordinates.add(new LatLng(13.114605,77.635293));
        defaultCoordinates.add(new LatLng(13.114673,77.634885));
        defaultCoordinates.add(new LatLng(13.113907,77.634604));
        defaultCoordinates.add(new LatLng(13.113950,77.635636));
        defaultCoordinates.add(new LatLng(13.115670,77.636016));
        defaultCoordinates.add(new LatLng(13.115664,77.635998));
        defaultCoordinates.add(new LatLng(13.114886,77.635889));
        defaultCoordinates.add(new LatLng(13.116095,77.634932));
        //defaultCoordinates.add(new LatLng(13.116095,77.634932));
        defaultCoordinates.add(new LatLng(13.116201,77.63537));

       

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check if mMap is null before using it
        if (mMap != null) {
            // Add marker for start position
            mMap.addMarker(new MarkerOptions().position(new LatLng(startLat, startLng)));

            // Start location updates
            startLocationUpdates();

            // Listener to retrieve data from the database
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        // Do nothing related to drawing polyline here
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
        String dateTime = getCurrentDateTime();
        // Check if user's location is near any of the default points
        for (LatLng coordinate : defaultCoordinates) {
            float[] distance = new float[1];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                    coordinate.latitude, coordinate.longitude, distance);
            if (distance[0] < 20) { // Adjust this value as needed for your accuracy requirements
                // Place a marker at the coordinate
                MarkerOptions markerOptions = new MarkerOptions().position(coordinate);
                if (coordinate.latitude == startLat && coordinate.longitude == startLng) {
                    // Green color for start position
                    markerOptions.icon(getMarkerIcon(Color.GREEN));
                } else {
                    // Store the last visited location details in Firebase\
                    DatabaseReference lastVisitedRef ;
                    lastVisitedRef = mDatabase.child("LastVisited").child(srn);
                    lastVisitedRef.child("SRN").setValue(srn);
                    lastVisitedRef.child("latitude").setValue(coordinate.latitude);
                    lastVisitedRef.child("longitude").setValue(coordinate.longitude);
                    lastVisitedRef.child("Date-Time").setValue(dateTime);

                    // Check if this location is last visited
                    lastVisitedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Double lastVisitedLatitude = dataSnapshot.child("latitude").getValue(Double.class);
                                Double lastVisitedLongitude = dataSnapshot.child("longitude").getValue(Double.class);

                                if (lastVisitedLatitude != null && lastVisitedLongitude != null &&
                                        lastVisitedLatitude.equals(coordinate.latitude) && lastVisitedLongitude.equals(coordinate.longitude)) {
                                    // Green color for last visited location
                                    mMap.addMarker(markerOptions.icon(getMarkerIcon(Color.GREEN)));
                                } else {
                                    // Red color for other locations
                                    mMap.addMarker(markerOptions.icon(getMarkerIcon(Color.RED)));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error
                        }
                    });
                    return;
                }
                // Add the marker to the map
                mMap.addMarker(markerOptions);
            }
        }
    }


    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
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