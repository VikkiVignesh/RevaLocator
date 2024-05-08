package com.example.revalocator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
    private double defaultLat = 13.114750;
    private double defaultLng = 77.634684;

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check if mMap is null before using it
        if (mMap != null) {
            // Listener to retrieve data from the database
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        // Loop through all marker locations
                        for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                            Double latitude = markerSnapshot.child("latitude").getValue(Double.class);
                            Double longitude = markerSnapshot.child("longitude").getValue(Double.class);

                            // Check if latitude and longitude are not null
                            if (latitude != null && longitude != null) {
                                // Check if the retrieved coordinates match the default coordinates
                                if (latitude.equals(defaultLat) && longitude.equals(defaultLng)) {
                                    // Coordinates match, draw a graph from default location to this location
                                    drawGraph(defaultLat, defaultLng, latitude, longitude);
                                }
                            }
                        }
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

    private void drawGraph(double startLat, double startLng, double endLat, double endLng) {
        try {
            // Check if mMap is null before using it
            if (mMap != null) {
                List<LatLng> points = new ArrayList<>();
                points.add(new LatLng(startLat, startLng));
                points.add(new LatLng(endLat, endLng));

                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(points)
                        .color(Color.RED)
                        .width(5);

                Polyline polyline = mMap.addPolyline(polylineOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLat, startLng), 12));
            } else {
                // Handle the case where mMap is null
                Toast.makeText(getContext(), "Map is not available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during graph drawing
            e.printStackTrace();
        }
    }
}

