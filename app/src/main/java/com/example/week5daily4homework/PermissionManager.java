package com.example.week5daily4homework;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class PermissionManager {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    IPermissionManager iPermissionManager;
    Context context;

    public PermissionManager(Context context){
        this.iPermissionManager = (IPermissionManager)context;
        this.context = context;
    }

    public void checkPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission((Activity)context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                requestPermission();
            }
        } else {
            // Permission has already been granted
            iPermissionManager.onPermissionResult(true);
        }
    }

    public void requestPermission(){
        Log.d("TAG", "onCreate: No explaination needed; request the permission");
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    public void checkResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length>0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    Log.d("TAG", "onRequestPermissionResult: permission was granted");
                    // permission was granted, yay! Do the contacts-related task you need to do.

                    iPermissionManager.onPermissionResult(true);
                } else {
                    iPermissionManager.onPermissionResult(false);
                    Log.d("TAG", "onRequestPermissionsResult: permissions denied");
                }
                return;
            }
        }
    }

    public interface IPermissionManager{
        void onPermissionResult(boolean isGranted);
    }
}
