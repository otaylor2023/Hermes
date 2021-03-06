package com.hermes.ui.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hermes.AddNewContact;
import com.hermes.R;
import com.hermes.storage.ContactPOJO;
import com.hermes.storage.LocalStorage;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ContactPOJO> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecyclerViewAdapter(List<ContactPOJO> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactPOJO contact = mData.get(position);
        holder.myTextView.setText(contact.getName() + "\n" + contact.getNumber() + "\n" + contact.getMessage());
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                System.out.println("Deleting Contact");
                LocalStorage.deleteContact(view, contact.getName());
                updateDataList(view);
//                mData = LocalStorage.getContactList(view);
//                System.out.println(LocalStorage.getContactList(view));
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, mData.size());
            }
        });


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView btEdit, btDelete;
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(this);
            btDelete = itemView.findViewById(R.id.bt_delete);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id).getName();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void updateDataList(View view) {
        mData = LocalStorage.getContactList(view);
        notifyDataSetChanged();
    }

    public ContactPOJO getContactAtPosition(int pos) {
        return mData.get(pos);
    }
}