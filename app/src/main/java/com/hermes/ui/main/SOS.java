package com.hermes.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hermes.R;
import com.hermes.databinding.FragmentSOSBinding;



/**
 * A placeholder fragment containing a simple view.
 */
public class SOS extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "mercury.sos";

    private PageViewModel pageViewModel;
    private FragmentSOSBinding binding;

    public static final String ACCOUNT_SID = "ACdd221cd7d78a93ce9231963bafff46cf";
    public static final String AUTH_TOKEN = "e49defa5976787661a01becd2dd7bdaa";

    public static SOS newInstance(int index) {
        SOS fragment = new SOS();
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

        binding = FragmentSOSBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button messageButton = view.findViewById(R.id.messageButton);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(
                            getContext(), Manifest.permission.SEND_SMS) ==
                            PackageManager.PERMISSION_GRANTED) {
                        sendEmergencyMessages(view);
                    }
                    else{
                        requestPermissionLauncher.launch(
                                Manifest.permission.SEND_SMS);
                    }
                }
                //sendEmergencyMessages(view);
            }
        });


        Button fakeCall = view.findViewById(R.id.fakeCall);
        fakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(
                            getContext(), Manifest.permission.CALL_PHONE) ==
                            PackageManager.PERMISSION_GRANTED) {
                        makePhoneCall(view);
                    }
                    else{
                        requestPermissionLauncher.launch(
                                Manifest.permission.CALL_PHONE);
                    }
                }
                //sendEmergencyMessages(view);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    public void makePhoneCall(View view){
        String number = "9738735376";
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:"+number));
        startActivity(i);
    }

    public void sendEmergencyMessages(View view) {
        String sPhone = "9738735376";
        String etMessage = "Test";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sPhone, null, etMessage, null, null);

            //Toast.makeText(getApplicationContext(), "SMS sent.",
              //      Toast.LENGTH_LONG).show();
        }

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

}
