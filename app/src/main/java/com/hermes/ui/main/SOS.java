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
import android.widget.TextView;

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
import com.hermes.storage.ContactPOJO;
import com.hermes.storage.LocalStorage;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class SOS extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "mercury.sos";

    private PageViewModel pageViewModel;
    private FragmentSOSBinding binding;
    private TextView first, second, third;

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
        first = root.findViewById(R.id.textView5);
        second = root.findViewById(R.id.textView6);
        third = root.findViewById(R.id.textView7);
        List<ContactPOJO> contacts = LocalStorage.getContactList(root);
        String str = "Sends Emergency Text Message to:\n";
        for(int i = 0; i < contacts.size(); i++){
            str += (i+1) + ". " + contacts.get(i).getName() + "\n";
        }
        first.setText("Calls 911\n" + str);
        second.setText(str);
        third.setText("Sends emergency text messages if you do not check into the app after a certain time.");

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


        Button panic = view.findViewById(R.id.panicButton);
        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(
                            getContext(), Manifest.permission.CALL_PHONE) ==
                            PackageManager.PERMISSION_GRANTED) {
                        makePhoneCall(view);
                        sendEmergencyMessages(view);
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
        List<ContactPOJO> contacts = LocalStorage.getContactList(view);
        SmsManager smsManager = SmsManager.getDefault();
        for(int i = 0; i < contacts.size(); i++){
            smsManager.sendTextMessage(contacts.get(i).getNumber(), null, contacts.get(i).getMessage(), null, null);
        }
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
