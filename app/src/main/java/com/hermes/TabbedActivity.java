package com.hermes;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hermes.HermesUtils;
import com.hermes.R;
import com.hermes.storage.HermesStorage;
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
    private HermesStorage hermesStorage;

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
        hermesStorage = new HermesStorage();
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
                beginSignIn(view);
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
        hermesStorage.attemptSignIn(username, unhashedPassword, new OnLoginCheckedCallback() {
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

}