package com.hermes;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static String TAG = "mercury.maps_fragment";
    //private Toolbar toolbar;
    private GoogleMap mMap;

    public static MapsFragment newInstance(int index) {
        MapsFragment fragment = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d(TAG, "mMap ready");
            mMap = googleMap;
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker out Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        initializeButtons(view);
    }

    public void initializeButtons(View view) {
        ImageButton addButton = view.findViewById(R.id.imageButton2);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCrime(view);
            }
        });


    }


    public void addCrime(View view) {
        if (mMap == null) {
            Log.d(TAG, "mMap null");
            return;
        }
        Log.d(TAG, "called");
//        LatLng sydneyish = new LatLng(-35, 152);
//        mMap.addMarker(new MarkerOptions().position(sydneyish).title("Test marker"));
        ImageButton addButton = view.findViewById(R.id.imageButton2);
        addButton.setVisibility(View.INVISIBLE);
        LayoutInflater inflater = (LayoutInflater)
                view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

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
//
//        // dismiss the popup window when touched
        Button confirmButton = popupWindow.getContentView().findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View popupView) {
                finishLocation(view, popupWindow);

            }
        });
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

    public void finishLocation(View mainView, PopupWindow currentPopup) {
        currentPopup.dismiss();
        ImageButton addButton = mainView.findViewById(R.id.imageButton2);
        addButton.setVisibility(View.VISIBLE);
        addCrimeInformation(mainView);
    }



    public void addCrimeInformation(View view) {
        LayoutInflater inflater = (LayoutInflater)
                view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View directionView = inflater.inflate(R.layout.crime_details_window, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        final PopupWindow infoWindow = new PopupWindow(directionView, width, height, focusable);
        infoWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//        Spinner spinner = findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.crime_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);

    }
}