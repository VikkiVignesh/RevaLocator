package com.example.revalocator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.example.revalocator.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsFragment extends Fragment implements LocationListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private Marker myMarker;
    private Context mContext;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        get_Location();
    }

//    private void get_Location() {
//        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
//        } else {
//            // Inside onRequestPermissionsResult() method, after checking location permissions
//            showLocationTurnDialog();
//            // Inside onCreate() method, after checking location settings
//            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
//            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        // Use the location object to get latitude and longitude
//                        double latitude = location.getLatitude();
//                        double longitude = location.getLongitude();
//                        LatLng myLoc = new LatLng(latitude, longitude);
//                        mMap.addMarker(new MarkerOptions().position(myLoc).title("My Location").icon(bitdescriber(mContext, R.drawable.home)));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 20));
//                        // Do something with the obtained latitude and longitude
//                        Toast.makeText(mContext, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Unable to retrieve location
//                        Toast.makeText(mContext, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }).addOnFailureListener(getActivity(), new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    // Location retrieval failed
//                    Toast.makeText(mContext, "Location retrieval failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
    private void get_Location() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true); // Enable showing user's location on the map
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
            if (myMarker != null) {
                myMarker.setPosition(myLoc);
            } else {
                myMarker = mMap.addMarker(new MarkerOptions().position(myLoc).title("My Location").icon(bitdescriber(mContext, R.drawable.home)));
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 20));
        }
    }

    // Method to show dialog to turn on location services
    private void showLocationTurnDialog() {
        if (!isGPSEnabled(mContext)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Location services are disabled. Do you want to enable them?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Open location settings
                            Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(enableLocationIntent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Dismiss the dialog
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private BitmapDescriptor bitdescriber(Context ctx, int vectorread) {
        Drawable vectordraw = ContextCompat.getDrawable(ctx, vectorread);
        vectordraw.setBounds(0, 0, vectordraw.getIntrinsicWidth(), vectordraw.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectordraw.getIntrinsicWidth(), vectordraw.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectordraw.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                get_Location();
            } else {
                Toast.makeText(mContext, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
