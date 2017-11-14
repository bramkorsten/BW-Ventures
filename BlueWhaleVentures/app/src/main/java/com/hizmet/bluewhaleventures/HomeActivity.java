package com.hizmet.bluewhaleventures;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
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
    Fragment currentFragment;

    // Bottom navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_information:

                    if (fragmentManager.findFragmentByTag(informationFragment.getTag()) == null) {
                        transaction.add(R.id.content, informationFragment, informationFragment.getClass().getSimpleName());
                    }

                    transaction.hide(currentFragment);
                    transaction.show(informationFragment);
                    transaction.commit();
                    currentFragment = informationFragment;
                    return true;
                case R.id.navigation_experiments:

                    if (fragmentManager.findFragmentByTag(experimentsFragment.getTag()) == null) {
                        transaction.add(R.id.content, experimentsFragment, experimentsFragment.getClass().getSimpleName());
                    }

                    transaction.hide(currentFragment);
                    transaction.show(experimentsFragment);
                    transaction.commit();
                    currentFragment = experimentsFragment;
                    return true;
                case R.id.navigation_settings:

                    if (fragmentManager.findFragmentByTag(settingsFragment.getTag()) == null) {
                        transaction.add(R.id.content, settingsFragment, settingsFragment.getClass().getSimpleName());
                    }

                    transaction.hide(currentFragment);
                    transaction.show(settingsFragment);
                    transaction.commit();
                    currentFragment = settingsFragment;
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

        transaction.add(R.id.content, experimentsFragment, experimentsFragment.getClass().getSimpleName()).show(experimentsFragment).commit();
        currentFragment = experimentsFragment;
    }
}
