package com.hizmet.bluewhaleventures;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hizmet.bluewhaleventures.fragments.ExperimentsFragment;
import com.hizmet.bluewhaleventures.fragments.InformationFragment;
import com.hizmet.bluewhaleventures.fragments.SettingsFragment;

public class HomeActivity extends AppCompatActivity {

    ExperimentsFragment experimentsFragment;
    InformationFragment informationFragment;
    SettingsFragment settingsFragment;

    // Bottom navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_information:
                    // Go to the Information Fragment
                    transaction.replace(R.id.content, informationFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_experiments:
                    // Go to the Experiments Fragment
                    transaction.replace(R.id.content, experimentsFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_settings:
                    // Go to the Settings Fragment
                    transaction.replace(R.id.content, settingsFragment);
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationExperiments);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem navItem2 = navigation.getMenu().findItem(R.id.navigation_experiments);
        navItem2.setChecked(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Go to the Experiments Fragment

        experimentsFragment = new ExperimentsFragment();
        informationFragment = new InformationFragment();
        settingsFragment = new SettingsFragment();

        transaction.replace(R.id.content, experimentsFragment);
        transaction.commit();
    }
}
