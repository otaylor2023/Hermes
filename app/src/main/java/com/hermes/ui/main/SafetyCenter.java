package com.hermes.ui.main;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hermes.AddNewContact;
import com.hermes.HermesUtils;
import com.hermes.PlaceholderFragment;
import com.hermes.R;
import com.hermes.databinding.FragmentSOSBinding;
import com.hermes.databinding.FragmentSafetyCenterBinding;
import com.hermes.databinding.FragmentTabsBinding;
import com.hermes.storage.ContactPOJO;
import com.hermes.storage.LocalStorage;
import com.hermes.storage.OnContactsChangedCallback;

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
    FloatingActionButton add;
    RecyclerView recyclerView;
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
        Log.d(TAG, "safety view created");
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentSafetyCenterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //HermesUtils.createFakeContacts(root);

        add = root.findViewById(R.id.bt_add);
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(LocalStorage.getContactList(root));
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "safety view created");
//        LocalStorage.addContactChangedListener(root, new OnContactsChangedCallback() {
//            @Override
//            public void onContactsChanged(List<ContactPOJO> contactPOJOList) {
//                updateContactList(root, contactPOJOList);
//            }
//        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), AddNewContact.class);
                startActivity(intent);
                //dataList = LocalStorage.getContactList(root);



            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateDataList(getView());
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(getActivity(), dataList.toString(), Toast.LENGTH_SHORT).show();
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        // create the popup window
        View editContact = inflater.inflate(R.layout.popupeditcontact, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(editContact, width, height, focusable);


        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        Button save = editContact.findViewById(R.id.submitButton);
        EditText name = editContact.findViewById(R.id.name);
        EditText number = editContact.findViewById(R.id.number);
        EditText message = editContact.findViewById(R.id.messageEdit);
        name.setOnFocusChangeListener(HermesUtils.getTextFocusListener(R.id.name));
        number.setOnFocusChangeListener(HermesUtils.getTextFocusListener(R.id.number));
        message.setOnFocusChangeListener(HermesUtils.getTextFocusListener(R.id.messageEdit));
        ContactPOJO targetContact = adapter.getContactAtPosition(position);
        name.setText(targetContact.getName());
        number.setText(targetContact.getNumber());
        message.setText(targetContact.getMessage());

        // dismiss the popup window when touched
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Deleting Contact");
                LocalStorage.deleteContact(view, targetContact.getName());
                LocalStorage.saveContact(view, new ContactPOJO(name.getText().toString(), number.getText().toString(), message.getText().toString()));
                adapter.updateDataList(view);
                popupWindow.dismiss();
            }
        });
    }

}