package com.hermes;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hermes.ui.main.SectionsPagerAdapter;
import com.hermes.databinding.ActivityTabsBinding;

public class tabs extends AppCompatActivity {

    private static String TAG = "mercury.tabs";

    private ActivityTabsBinding binding;
    private MapsFragment mapsFragment;

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
    }

    public void sendEmergencyMessages(View view) {
        String sPhone = "9738735376";
        String etMessage = "Test";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sPhone, null, etMessage, null, null);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    // region maps methods
//
//    public void addCrime(View view) {
////        Log.d(TAG, "add crime tabs");
////        mapsFragment.addCrime(view);
//    }
//
//    public void finishLocation(View view) {
//        Log.d(TAG, "finish location tabs");
//        mapsFragment.finishLocation(view);
//    }

    // endregion maps methods
}