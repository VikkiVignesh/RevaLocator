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
import android.util.Log;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;

import com.example.revalocator.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Map;


public class MapsFragment extends Fragment implements LocationListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
 String srn;
    private GoogleMap mMap;
    private Marker myMarker;
    private Context mContext;
    private DatabaseReference mDatabase;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private LocationManager locationManager;

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


        if(getArguments()!=null)
        {

            srn = getArguments().getString("srn");
        }


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

//        if (!checkLocationPermission()) {
//            requestLocationPermissions();
//        } else {
//            // Permissions are already granted, start the service
//            startLocationService();
//        }

        mDatabase = FirebaseDatabase.getInstance().getReference();




    }
//    private boolean checkLocationPermission() {
//        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//    }
//    private void requestLocationPermissions() {
//        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
//    }
//    private void startLocationService() {
//        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager != null) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        }
//    }




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
    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @Override
    public void onLocationChanged(Location location) {
        //LatLng myLoc = null;
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }
    private void updateMarkerPositionInDatabase(LatLng newPosition) {

        String dateTime = getCurrentDateTime();
        String locationName = getLocationName(newPosition.latitude, newPosition.longitude);
        DatabaseReference markerLocationsRef = mDatabase.child("markerLocations");

        // Check if the SRN is not null
        if (srn != null) {
            // Create a child node with the SRN as the key
            DatabaseReference srnRef = markerLocationsRef.child(srn);

            // Update the child node with the new location and date/time
            srnRef.child("latitude").setValue(newPosition.latitude);
            srnRef.child("longitude").setValue(newPosition.longitude);
            srnRef.child("locationName").setValue(locationName);
            srnRef.child("dateTime").setValue(dateTime);
        } else {
            // Handle the case where SRN is null
            // You might want to handle this situation based on your app's requirements
        }
    }


    private String getLocationName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append(" ");
                }
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown Location";
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
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

//    private void storeUserData(String srn, String location ,String latitude ,String  longitude ,String date_time) {
//        // Get a reference to the location where the user data will be stored
//        DatabaseReference userDataRef = mDatabase.child("users").child(srn);
//
//        // Create a User object with the provided data
//        Users user = new Users(srn , location ,latitude , longitude , date_time);
//
//        // Set the value of the user data in the database
//        userDataRef.setValue(user);
//    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

}
