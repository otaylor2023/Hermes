package com.hermes;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hermes.storage.ServerStorage;
import com.hermes.storage.LocalStorage;
import com.hermes.storage.OnLoginCheckedCallback;
import com.hermes.storage.OrgPOJO;
import com.hermes.databinding.ActivityTabsBinding;
import com.hermes.ui.main.MapsFragment;
import com.hermes.ui.main.SectionsPagerAdapter;

public class TabbedActivity extends AppCompatActivity {

    private static String TAG = "mercury.TabbedActivity";

    private ActivityTabsBinding binding;
    private MapsFragment mapsFragment;
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTabsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        tabs.selectTab(tabs.getTabAt(1));
        mapsFragment = (MapsFragment) sectionsPagerAdapter.getItem(1);


//        FloatingActionButton fab = binding.fab;
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final View rootView = getWindow().getDecorView().getRootView();

        //by now all views will be displayed with correct values
//                        LocalStorage.signUserOut(rootView);
//        HermesUtils.createFakeContacts(rootView);
//        LocalStorage.deleteContact(rootView, "person1");
//        LocalStorage.deleteContact(rootView, "person2");
//        LocalStorage.deleteContact(rootView, "person3");
        HermesUtils.viewContacts(rootView);
        setCurrentUser(LocalStorage.getCurrentUser(rootView));
//                        HermesUtils.createFakeOrg();

        rootView.findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSignIn(view);
            }
        });

//        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    @Override
//                    public void onGlobalLayout() {
//
//
//
//                    }
//                });
    }

    public void checkSignIn(View mainView) {
        OrgPOJO orgPOJO = LocalStorage.getCurrentUser(mainView);
        if (orgPOJO == null) {
            beginSignIn(mainView);
            return;
        }

        LayoutInflater inflater = (LayoutInflater)
                mainView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View loggedInView = inflater.inflate(R.layout.account_info_popup, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow loggedInWindow = new PopupWindow(loggedInView, width, height, focusable);
        loggedInWindow.showAtLocation(mainView, Gravity.CENTER, 0, 0);

        TextView displayNameBox = loggedInView.findViewById(R.id.detailDisplayName);
        displayNameBox.setText(orgPOJO.getDisplayName());

        TextView usernameBox = loggedInView.findViewById(R.id.detailUsername);
        usernameBox.setText(orgPOJO.getUsername());

        Button signOutButton = loggedInView.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalStorage.signUserOut(view);
                Button signInButton = mainView.findViewById(R.id.signInButton);
                signInButton.setText("Sign In");
                loggedInWindow.dismiss();
            }
        });
    }




    public void beginSignIn(View mainView) {

        LayoutInflater inflater = (LayoutInflater)
                mainView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View signInView = inflater.inflate(R.layout.sign_in_popup, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow logInWindow = new PopupWindow(signInView, width, height, focusable);
        logInWindow.showAtLocation(mainView, Gravity.CENTER, 0, 0);

        EditText usernameBox = signInView.findViewById(R.id.usernameBox);
        usernameBox.setOnFocusChangeListener(HermesUtils.getTextFocusListener(R.id.usernameBox));

        EditText passwordBox = signInView.findViewById(R.id.passwordBox);
        passwordBox.setOnFocusChangeListener(HermesUtils.getTextFocusListener(R.id.passwordBox));

        signInView.findViewById(R.id.logInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignIn(signInView, usernameBox, passwordBox, logInWindow);
            }
        });

    }


    public void attemptSignIn(View logInView, EditText usernameBox, EditText passwordBox, PopupWindow logInWindow) {
        String username = usernameBox.getText().toString();
        String unhashedPassword = passwordBox.getText().toString();
        ServerStorage.attemptSignIn(username, unhashedPassword, new OnLoginCheckedCallback() {
            @Override
            public void onLoginChecked(OrgPOJO match) {
                if (match == null) {
                    logInView.findViewById(R.id.incorrectBox).setVisibility(View.VISIBLE);
                    passwordBox.setText("");
                } else {
                    successfulSignIn(match, logInWindow);
                }
            }
        });
    }

    public void successfulSignIn(OrgPOJO orgPOJO, PopupWindow logInWindow) {
        logInWindow.dismiss();
        LocalStorage.storeCurrentUser(getWindow().getDecorView().getRootView(), orgPOJO);
        setCurrentUser(orgPOJO);
    }

    public void setCurrentUser(OrgPOJO orgPOJO) {
        if (orgPOJO != null) {
            Button signInButton = getWindow().getDecorView().getRootView().findViewById(R.id.signInButton);
            signInButton.setText(orgPOJO.getDisplayName());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    Log.d(TAG, "location permissions granted");
                    mapsFragment.setLocationPermission(locationPermissionGranted);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "resumed");
    }
}