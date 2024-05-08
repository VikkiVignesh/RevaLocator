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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Map;

public class MapsFragment extends Fragment implements LocationListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private Marker myMarker;
    private Context mContext;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    int i=0;
    LocationStorage SavedLocation=new LocationStorage();




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        //storing locations lat & long
        /*
        SavedLocation.addLocation("Library",13.114605,77.635293);
        SavedLocation.addLocation("Rangasthala",13.114673,77.634885);
        SavedLocation.addLocation("Admin Block",13.113907,77.634604);
        SavedLocation.addLocation("Applied Science",13.113950,77.635636);
        SavedLocation.addLocation("Saugandhika",13.115670,77.636016);
        SavedLocation.addLocation("Ground",13.116570,77.636176);
        SavedLocation.addLocation("Food Court",13.115664,77.635998);
        SavedLocation.addLocation("Coffee",13.114886,77.635889);
        SavedLocation.addLocation("Maggie Point",13.116095,77.634932);
        SavedLocation.addLocation("Nandhini",13.116201,77.635376);
        */
        SavedLocation.addLocation("A",13.116774,77.631275);
        SavedLocation.addLocation("B",13.116769,77.631232);
        SavedLocation.addLocation("C",13.116765,77.631255);
        SavedLocation.addLocation("D",13.116777,77.631258);
        SavedLocation.addLocation("E",13.116787,77.631282);
        SavedLocation.addLocation("F",13.116746,77.631256);
        mContext = getContext();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        get_Location();

    }

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
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            if (myMarker != null) {
                //Retriving srn from login page
                String srn = getActivity().getIntent().getStringExtra("SRN");
                // Check if the user's location is within the desired distance of any specified location
                for (Map.Entry<String, LatLng> entry : SavedLocation.getLocations().entrySet()) {
                    LatLng savedLoc = entry.getValue();
                    float[] distance = new float[1];
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                            savedLoc.latitude, savedLoc.longitude, distance);
                    // Check if the distance is within the desired range (10-20m)
                    if (distance[0] >= 10 && distance[0] <= 20) {
                        // Update the marker on the map
                        LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
                        if (myMarker != null) {
                            myMarker.setPosition(myLoc);
                        } else {
                            mMap.addMarker(new MarkerOptions().position(myLoc).title("Student - " + srn).icon(bitdescriber(mContext, R.drawable.std_marker)));
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 20));

                        // Push the latitude and longitude data to Firebase
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Student_locations");

                        String locKey = "Loc" + i++;
                        databaseReference.child(srn).child(locKey).setValue(new GeoLocation(location.getLatitude(), location.getLongitude()));
                        break; // Exit the loop after storing the location
                    }
                }

            }
            else {
                String locationString = String.format("Latitude: %f, Longitude: %f", userLocation.latitude, userLocation.longitude);
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .title("Current Location")
                        .snippet(locationString)
                        .icon(bitdescriber(mContext, R.drawable.currloc)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
