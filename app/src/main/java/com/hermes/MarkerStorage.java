package com.hermes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerStorage {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final String storagePath = "markerStorage";
    private static final String TAG = "mercury.markerstorage";

    public MarkerStorage() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void addMarker(MarkerPOJO markerPOJO) {
        String key = reference.child(storagePath).push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put(key, markerPOJO);
        reference.child(storagePath).updateChildren(map);
    }

    public void getMarkers(OnMarkersReceivedCallback markersReceivedCallback) {
        Query query = reference.child(storagePath).limitToFirst(100);
        Task<DataSnapshot> snapshotTask = query.get();
        snapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(@NonNull DataSnapshot dataSnapshot) {
                List<MarkerData> markerDataList = new ArrayList<>();
                Log.d(TAG, "success");


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, snapshot.getKey() + " " + snapshot.getValue().toString());

                    MarkerPOJO markerPOJO = snapshot.getValue(MarkerPOJO.class);
                    markerDataList.add(new MarkerData(markerPOJO));
                    Log.d(TAG, markerPOJO.getLatitude() + " " + markerPOJO.getLongitude());

//                    markerDataList.addAll(data.values());
//                    System.out.println(markerDataList);
                }
                markersReceivedCallback.onReceived(markerDataList);
            }
        });
    }

}
