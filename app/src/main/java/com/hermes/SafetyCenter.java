package com.hermes;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hermes.databinding.FragmentSafetyCenterBinding;
import com.hermes.ui.main.PageViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SafetyCenter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SafetyCenter extends Fragment {


    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "mercury.safetyCenterFrag";



    private PageViewModel pageViewModel;
    private FragmentSafetyCenterBinding binding;

    public static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;

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





    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentSafetyCenterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        FloatingActionButton messageButton = root.findViewById(R.id.fab);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewContact.class);
                startActivity(intent);
            }
        });

        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Button testButton = view.findViewById(R.id.button2);
//        testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "button clicked");
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}