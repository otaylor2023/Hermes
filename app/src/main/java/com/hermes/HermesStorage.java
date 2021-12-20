package com.hermes;

import android.util.Log;

import androidx.annotation.NonNull;

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

public class HermesStorage {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final String TAG = "mercury.markerstorage";

    public HermesStorage() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void addMarker(MarkerPOJO markerPOJO) {
        String key = reference.child(Storage.MARKER_STORAGE.path).push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put(key, markerPOJO);
        reference.child(Storage.MARKER_STORAGE.path).updateChildren(map);

    }

    public void getMarkers(OnMarkersReceivedCallback markersReceivedCallback) {
        Query query = reference.child(Storage.MARKER_STORAGE.path).limitToFirst(100);
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

    public void attemptSignIn(String username, String unhashedPassword, OnLoginCheckedCallback loginCheckedCallback) {
        Query query = reference.child(Storage.LOGIN_STORAGE.path).orderByKey();
        Task<DataSnapshot> snapshotTask = query.get();
        snapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, OrgPOJO> userDataList;
                Log.d(TAG, "success");
                GenericTypeIndicator<HashMap<String, OrgPOJO>> typeIndicator = new GenericTypeIndicator<HashMap<String, OrgPOJO>>() {};
                userDataList = dataSnapshot.getValue(typeIndicator);
                OrgPOJO match = checkForMatch(username, unhashedPassword, userDataList);
                loginCheckedCallback.onLoginChecked(match);
            }
        });
    }

    /**
     * Checks the user data list for a match and returns the match or null if there isnt one
     * @param username the username to check
     * @param unhashedPassword the unhashed password
     * @param userDataList the user data list
     * @return the match if a match is found, otherwise a null object
     */
    private OrgPOJO checkForMatch(String username, String unhashedPassword, HashMap<String, OrgPOJO> userDataList) {
        if (userDataList != null) {
            OrgPOJO match = userDataList.get(username);
            if (match != null && match.checkPassword(unhashedPassword)) {
                return match;
            }
        }
        return null;
    }

    public void addOrganization(OrgPOJO orgPOJO) {
        String key = orgPOJO.getUsername();
        Map<String, Object> map = new HashMap<>();
        map.put(key, orgPOJO);
        reference.child(Storage.LOGIN_STORAGE.path).updateChildren(map);
    }

    public enum Storage {
        MARKER_STORAGE("markerStorage"),
        LOGIN_STORAGE("login_storage");


        public final String path;

        Storage(String path) {
            this.path = path;
        }
    }
}
