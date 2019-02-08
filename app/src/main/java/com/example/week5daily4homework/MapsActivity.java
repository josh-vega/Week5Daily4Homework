package com.example.week5daily4homework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        PermissionManager.IPermissionManager,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    PermissionManager permissionManager;
    private static long UPDATE_INTERVAL = 5000;
    private static long FASTEST_INTERVAL = 5000;

    private GoogleMap mMap;
    Location currentUserLocation;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        permissionManager = new PermissionManager(this);
        permissionManager.checkPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Wendy's
        LatLng wendys = new LatLng(33.038438,-85.0356268);
        mMap.addMarker(new MarkerOptions().position(wendys).title("Wendy's").snippet(getCurrentLocationAddress(wendys)).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.wendys)));

        //Add a marker on McDonald's
        LatLng mcDonald = new LatLng(33.049036799999996,-85.0236302);
        mMap.addMarker(new MarkerOptions().position(mcDonald).title("McDonald's").snippet(getCurrentLocationAddress(mcDonald)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

        //Add a marker on Burger King
        LatLng burgerKing = new LatLng(33.057853,-85.0291518);
        mMap.addMarker(new MarkerOptions().position(burgerKing).title("Burger King").snippet(getCurrentLocationAddress(burgerKing)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        //Add a marker on Waffle House
        LatLng waffleHouse = new LatLng(33.0550363,-85.02890819999999);
        mMap.addMarker(new MarkerOptions().position(waffleHouse).title("Waffle House").snippet(getCurrentLocationAddress(waffleHouse)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        //Add a marker on Taco Bell
        LatLng tacoBell = new LatLng(33.0490358,-85.02801509);
        mMap.addMarker(new MarkerOptions().position(tacoBell).title("Taco Bell").snippet(getCurrentLocationAddress(tacoBell)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(tacoBell));
        mMap.setMinZoomPreference(15);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onPermissionResult(boolean isGranted) {
        if(isGranted){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("MissingPermission")
    private void getLastKnownLocation(){
        FusedLocationProviderClient fusedLocationProviderClient = getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    Log.d("TAG", "onSuccess: " + location.toString());
                    currentUserLocation = location;
                    //moveToNewLocation(location, "CURRENT PHONE LOC");
                } else {
                    Log.d("TAG", "onSuccess: LOCATION WAS NULL");
                }
            }
        });
    }

    private void moveToNewLocation(Location location, String locationName){
        if(location != null){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.setMinZoomPreference(15);
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setInterval(UPDATE_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("TAG", "onLocationResult: new location Recieved");
                onLocationChanged(locationResult.getLastLocation());
            }
        }, Looper.myLooper());
    }

    public String getCurrentLocationAddress(LatLng latLng){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            for(Address address : addresses){
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void getCurrentLatLngFromAddress(String address){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressWithLatLng = geocoder.getFromLocationName(address, 1);
            LatLng newLatLng = new LatLng(addressWithLatLng.get(0).getLatitude(), addressWithLatLng.get(0).getLongitude());
            Log.d("TAG", "getCurrentLatLngFromAddress: " + newLatLng.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
