package com.hermes.storage;

import androidx.annotation.NonNull;

import com.hermes.MarkerData;

import java.util.List;

public interface OnMarkersReceivedCallback {

    void onReceived(@NonNull List<MarkerData> markerDataList);
}
