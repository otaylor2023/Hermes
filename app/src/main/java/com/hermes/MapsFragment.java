package com.hermes;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static String TAG = "mercury.maps_fragment";
    //private Toolbar toolbar;
    private GoogleMap mMap;
    private HermesStorage markerStorage;

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
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    LinearLayout info = new LinearLayout(getContext());
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(getContext());
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(getContext());
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    Log.d(TAG, "START long" + marker.getPosition().longitude
                    + "lat: " + marker.getPosition().latitude);
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    Log.d(TAG, "DRAG long: " + marker.getPosition().longitude
                            + "lat: " + marker.getPosition().latitude);
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Log.d(TAG, "END long: " + marker.getPosition().longitude
                            + "lat: " + marker.getPosition().latitude);

                }
            });

            markerStorage = new HermesStorage();
            markerStorage.getMarkers(new OnMarkersReceivedCallback() {
                @Override
                public void onReceived(@NonNull List<MarkerData> markerDataList) {
                    for (MarkerData markerData : markerDataList) {
                        mMap.addMarker(createMarker(markerData, getView()));
                    }
                }
            });

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
                finishLocation(view, popupWindow, marker);

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

    public void finishLocation(View mainView, PopupWindow currentPopup, Marker marker) {
        currentPopup.dismiss();
        ImageButton addButton = mainView.findViewById(R.id.imageButton2);
        addButton.setVisibility(View.VISIBLE);
        addCrimeInformation(mainView, marker);
    }



    public void addCrimeInformation(View mainView, Marker marker) {
        MarkerData markerData = new MarkerData();

        LayoutInflater inflater = (LayoutInflater)
                mainView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View directionView = inflater.inflate(R.layout.crime_details_window, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow infoWindow = new PopupWindow(directionView, width, height, focusable);
        infoWindow.showAtLocation(mainView, Gravity.CENTER, 0, 0);

        infoWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                marker.remove();
            }
        });

        Button dateButton = directionView.findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View dirView) {
                showDatePickerDialog(dirView, dateButton, markerData);
            }
        });

        EditText descriptionBox = directionView.findViewById(R.id.descriptionBox);
        descriptionBox.setOnFocusChangeListener(HermesUtils.getTextFocusListener(R.id.descriptionBox));

//        Spinner spinner = directionView.findViewById(R.id.crimeSpinner);
//        Log.d(TAG, "spinner null? " + (spinner == null));

        Button submitButton = directionView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner = directionView.findViewById(R.id.crimeSpinner);
                if (spinner.getSelectedItemId() == R.string.spinner_default || markerData.getDate() == null) {
                    // TODO: 12/19/21 make this highlight the unfilled fields in red
                    return;
                }


                String selectedStr = (String) spinner.getSelectedItem();
                markerData.setCrimeType(MarkerData.getCrimeType(selectedStr, view));

                String description = descriptionBox.getText().toString();
                markerData.setDescription(description);
//                markerData.setResources(selectedStr);
                markerData.setLocation(marker.getPosition());
                Log.d(TAG, "SET long: " + marker.getPosition().longitude
                        + "lat: " + marker.getPosition().latitude);
                MarkerOptions markerOptions = createMarker(markerData, view);
                mMap.addMarker(markerOptions);
                markerStorage.addMarker(new MarkerPOJO(markerData));
                infoWindow.dismiss();


            }
        });

//        descriptionBox.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        descriptionBox.setRawInputType(InputType.TYPE_CLASS_TEXT);
//        Spinner spinner = findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.crime_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);

    }

    public MarkerOptions createMarker(MarkerData markerData, View view) {

        MarkerOptions markerOptions = new MarkerOptions()
                .position(markerData.getLocation())
                .draggable(false)
                .title(markerData.getCrimeString(view))
                .snippet(createSnippet(markerData))
                .icon(BitmapDescriptorFactory.defaultMarker(markerData.getCrimeColor(view)));
        return markerOptions;
    }

    public String createSnippet(MarkerData markerData) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        String dateStr = dateFormat.format(markerData.getDate().getTime());
//        return dateStr + "\n" + dateStr;
        return "Date: " + dateStr + "\n User description: " + markerData.getDescription();
    }

    public void showDatePickerDialog(View v, Button button, MarkerData markerData) {
        DialogFragment newFragment = new DatePickerFragment(button, markerData);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
}