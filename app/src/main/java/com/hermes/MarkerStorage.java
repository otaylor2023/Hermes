package com.hermes;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerStorage {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final String storagePath = "markerStorage";

    public MarkerStorage() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void addMarker(MarkerData markerData) {
        String key = reference.child(storagePath).push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put(key, markerData);
        reference.child(storagePath).updateChildren(map);
    }

    public List<MarkerData> getMarkers() {
        Query query = reference.child(storagePath).limitToFirst(100);
        Task<DataSnapshot> snapshotTask = query.get();
        DataSnapshot dataSnapshot = snapshotTask.getResult();
        List<MarkerData> markerDataList = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            MarkerData data = (MarkerData) snapshot.getValue();
            markerDataList.add(data);
        }

        return markerDataList;
    }
}
