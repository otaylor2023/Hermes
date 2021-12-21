package com.hermes.storage;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hermes.MarkerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerStorage {

    private static final String TAG = "mercury.markerstorage";

    public static void addMarker(MarkerPOJO markerPOJO) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        String key = reference.child(Storage.MARKER_STORAGE.path).push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put(key, markerPOJO);
        reference.child(Storage.MARKER_STORAGE.path).updateChildren(map);

    }


    public static void getMarkers(OnMarkersReceivedCallback markersReceivedCallback, boolean isRetry) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

        reference.child(Storage.MARKER_STORAGE.path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                markersReceivedCallback.onReceived(extractMarkerData(snapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (!isRetry) {
                    getMarkers(markersReceivedCallback, true);
                }
            }
        });
    }

    private static List<MarkerData> extractMarkerData(DataSnapshot dataSnapshot) {
        List<MarkerData> markerDataList = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Log.d(TAG, snapshot.getKey() + " " + snapshot.getValue().toString());

            MarkerPOJO markerPOJO = snapshot.getValue(MarkerPOJO.class);
            markerDataList.add(new MarkerData(markerPOJO));
            Log.d(TAG, markerPOJO.getLatitude() + " " + markerPOJO.getLongitude());

//                    markerDataList.addAll(data.values());
//                    System.out.println(markerDataList);
        }
        return markerDataList;
    }

    public static void attachMarkerListener(OnMarkersReceivedCallback markersReceivedCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.child(Storage.MARKER_STORAGE.path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                markersReceivedCallback.onReceived(extractMarkerData(snapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void attemptSignIn(String username, String unhashedPassword, OnLoginCheckedCallback loginCheckedCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        Query query = reference.child(Storage.LOGIN_STORAGE.path).orderByKey();
        Task<DataSnapshot> snapshotTask = query.get();
        snapshotTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, OrgPOJO> userDataList;
                Log.d(TAG, "success");
                GenericTypeIndicator<HashMap<String, OrgPOJO>> typeIndicator = new GenericTypeIndicator<HashMap<String, OrgPOJO>>() {};
                userDataList = dataSnapshot.getValue(typeIndicator);
                OrgPOJO match = ServerStorage.checkForMatch(username, unhashedPassword, userDataList);
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
    private static OrgPOJO checkForMatch(String username, String unhashedPassword, HashMap<String, OrgPOJO> userDataList) {
        if (userDataList != null) {
            OrgPOJO match = userDataList.get(username);
            if (match != null && match.checkPassword(unhashedPassword)) {
                return match;
            }
        }
        return null;
    }

    public static void addOrganization(OrgPOJO orgPOJO) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
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
