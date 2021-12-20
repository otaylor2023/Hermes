package com.hermes;

import androidx.annotation.NonNull;

import java.util.List;

public interface OnMarkersReceivedCallback {

    void onReceived(@NonNull List<MarkerData> markerDataList);
}
