package com.hermes.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hermes.MarkerData;
import com.hermes.R;
import com.hermes.storage.OnMarkersReceivedCallback;
import com.hermes.storage.ServerStorage;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<MarkerData> markerList;

    public FeedAdapter(List<MarkerData> dataSet) {
        markerList = dataSet;
        ServerStorage.attachMarkerListener(new OnMarkersReceivedCallback() {
            @Override
            public void onReceived(@NonNull List<MarkerData> markerDataList) {
                updateMarkerList(markerDataList);
            }
        });
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.feed_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        MarkerData markerData = markerList.get(position);
        viewHolder.getTypeBox().setText(String.format("Type: %s", markerData.getCrimeString(viewHolder.getView())));
        viewHolder.getSourceBox().setText(String.format("Source: %s", markerData.getOrganization()));
        viewHolder.getDateBox().setText(String.format("Date: %s", markerData.getDateStr()));
        viewHolder.getDescBox().setText(markerData.getDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return markerList.size();
    }

    public void updateMarkerList(List<MarkerData> markerDataList) {
        this.markerList = markerDataList;
        notifyDataSetChanged();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView typeBox;
        private final TextView dateBox;
        private final TextView sourceBox;
        private final TextView descBox;
        private final View view;


        public ViewHolder(View view) {
            super(view);
            this.view = view;
            // Define click listener for the ViewHolder's View

            typeBox = view.findViewById(R.id.typeFeedBox);
            dateBox = view.findViewById(R.id.dateFeedBox);
            sourceBox = view.findViewById(R.id.sourceFeedBox);
            descBox = view.findViewById(R.id.descFeedBox);

        }

        public TextView getTypeBox() {
            return typeBox;
        }

        public TextView getDateBox() {
            return dateBox;
        }

        public TextView getSourceBox() {
            return sourceBox;
        }

        public TextView getDescBox() {
            return descBox;
        }

        public View getView() {
            return view;
        }
    }



}

