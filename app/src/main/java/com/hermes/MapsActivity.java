package com.hermes;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hermes.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static String TAG = "mercury.mapsact";
//    private Toolbar toolbar;
    private PopupWindow currentPopup;
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //toolbar = findViewById();
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
        Log.d(TAG, "ready");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void addCrime(View view) {
        if (mMap == null) {
            return;
        }
        Log.d(TAG, "called");
//        LatLng sydneyish = new LatLng(-35, 152);
//        mMap.addMarker(new MarkerOptions().position(sydneyish).title("Test marker"));
        ImageButton addButton = findViewById(R.id.imageButton);
        addButton.setVisibility(View.INVISIBLE);
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        View directionView = inflater.inflate(R.layout.add_crime_instructions_window, null);
        //        LatLng sydneyish = new LatLng(-35, 152);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .title("Crime location")
                .position(mMap.getCameraPosition().target)
                .draggable(true)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
////
////        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(directionView, width, height, focusable);

//        // show the popup window
//        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        currentPopup = popupWindow;
        currentMarker = marker;
//
//        // dismiss the popup window when touched
//        Button but = findViewById(R.id.confirm_button);
//        findViewById(R.id.confirm_button).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                v.performClick();
//                ImageButton addButton = findViewById(R.id.imageButton);
//                addButton.setVisibility(View.VISIBLE);
//                findViewById(R.id.confirm_button).setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent rawEvent) {
//                        return false;
//                    }
//                });
//                popupWindow.dismiss();
//                return false;
//            }
//        });

    }

    public void addCrimeInformation(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View directionView = inflater.inflate(R.layout.crime_details_window, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        final PopupWindow infoWindow = new PopupWindow(directionView, width, height, focusable);
        infoWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    public void finishLocation(View view) {
        currentPopup.dismiss();
        ImageButton addButton = findViewById(R.id.imageButton);
        addButton.setVisibility(View.VISIBLE);
        addCrimeInformation(view);
    }



}