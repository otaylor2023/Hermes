package com.hermes.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hermes.PlaceholderFragment;
import com.hermes.R;
import com.hermes.databinding.FragmentSOSBinding;
import com.hermes.databinding.FragmentSafetyCenterBinding;
import com.hermes.databinding.FragmentTabsBinding;
import com.hermes.storage.ContactPOJO;
import com.hermes.storage.LocalStorage;
import com.hermes.storage.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SafetyCenter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SafetyCenter extends Fragment  implements RecyclerViewAdapter.ItemClickListener{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "mercury.safetycenter";

    private PageViewModel pageViewModel;
    private @NonNull FragmentSafetyCenterBinding binding;
    EditText editText;
    Button add;
    RecyclerView recyclerView;
    List<ContactPOJO> dataList = new ArrayList<>();
    RecyclerViewAdapter adapter;

    public static SafetyCenter newInstance(int index) {
        SafetyCenter fragment = new SafetyCenter();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentSafetyCenterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        editText = root.findViewById(R.id.edit_text);
        add = root.findViewById(R.id.bt_add);
        recyclerView = root.findViewById(R.id.recycler_view);
        dataList = LocalStorage.getContactList(root);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(getContext(), dataList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

//        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}