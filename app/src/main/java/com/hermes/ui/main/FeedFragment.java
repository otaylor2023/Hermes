package com.hermes.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hermes.MarkerData;
import com.hermes.R;
import com.hermes.databinding.FragmentFeedBinding;
import com.hermes.storage.HermesStorage;
import com.hermes.storage.OnMarkersReceivedCallback;

import java.util.List;

public class FeedFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "mercury.feedfrag";
    private FragmentFeedBinding binding;
    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    private HermesStorage hermesStorage;

    public static FeedFragment newInstance(int index) {
        FeedFragment fragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "feed fragment created");
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.crime_feed_recycler);
        this.hermesStorage = new HermesStorage();
        hermesStorage.getMarkers(new OnMarkersReceivedCallback() {
            @Override
            public void onReceived(@NonNull List<MarkerData> markerDataList) {

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new FeedAdapter(markerDataList);
                recyclerView.setAdapter(adapter);
                Log.d(TAG, "item number: " + adapter.getItemCount());
            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "feed fragment resumed");
        hermesStorage.getMarkers(new OnMarkersReceivedCallback() {
            @Override
            public void onReceived(@NonNull List<MarkerData> markerDataList) {
                adapter.updateMarkerList(markerDataList);
            }
        });
    }
}