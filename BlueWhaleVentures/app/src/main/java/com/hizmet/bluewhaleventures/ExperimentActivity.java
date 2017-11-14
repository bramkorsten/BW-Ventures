package com.hizmet.bluewhaleventures;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hizmet.bluewhaleventures.fragments.ExperimentFragment;
import com.hizmet.bluewhaleventures.fragments.PeopleFragment;
import com.hizmet.bluewhaleventures.fragments.SettingsFragment;

import java.util.Map;

public class ExperimentActivity extends AppCompatActivity {
    private Map experimentData;
    private String experimentId;

    PeopleFragment peopleFragment;
    ExperimentFragment experimentFragment;
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
                case R.id.navigation_people:

                    if (fragmentManager.findFragmentByTag(peopleFragment.getTag()) == null) {
                        transaction.add(R.id.content, peopleFragment, peopleFragment.getClass().getSimpleName());
                    }

                    transaction.hide(currentFragment);
                    transaction.show(peopleFragment);
                    transaction.commit();
                    currentFragment = peopleFragment;
                    return true;
                case R.id.navigation_experimentdetail:

                    if (fragmentManager.findFragmentByTag(experimentFragment.getTag()) == null) {
                        transaction.add(R.id.content, experimentFragment, experimentFragment.getClass().getSimpleName());
                    }

                    transaction.hide(currentFragment);
                    transaction.show(experimentFragment);
                    transaction.commit();
                    currentFragment = experimentFragment;
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
        setContentView(R.layout.activity_experiment);

        Intent intent = getIntent();
        experimentData = (Map) intent.getSerializableExtra("map");
        experimentId = intent.getStringExtra("id");

        // Bottom navigation
        BottomNavigationView mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationExperiment);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set menu item to People
        MenuItem navItem1 = mBottomNavigationView.getMenu().findItem(R.id.navigation_people);
        navItem1.setChecked(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        peopleFragment = new PeopleFragment();
        experimentFragment = new ExperimentFragment();
        settingsFragment = new SettingsFragment();

        // Go directly to the People Fragment
        transaction.add(R.id.content, peopleFragment, peopleFragment.getClass().getSimpleName()).show(peopleFragment).commit();
        currentFragment = peopleFragment;
    }

    public Map getExperimentDataFromParent() {
        return experimentData;
    }

    public String getExperimentIdFromParent() {
        return experimentId;
    }
}
