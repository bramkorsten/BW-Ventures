package com.hizmet.bluewhaleventures;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.hizmet.bluewhaleventures.fragments.ExperimentFragment;
import com.hizmet.bluewhaleventures.fragments.PeopleFragment;
import com.hizmet.bluewhaleventures.fragments.SettingsFragment;

import java.util.Map;

public class ExperimentActivity extends AppCompatActivity {
    private Map experimentData;
    private String experimentId;

    // Bottom navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_people:
                    // Go to the People Fragment
                    transaction.replace(R.id.content, new PeopleFragment()).commit();
                    return true;
                case R.id.navigation_experimentdetail:
                    // Go to the Experiment Fragment
                    transaction.replace(R.id.content, new ExperimentFragment()).commit();
                    return true;
                case R.id.navigation_settings:
                    // Go to the Settings Fragment
                    transaction.replace(R.id.content, new SettingsFragment()).commit();
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
        Log.d("ventures", "experimentID in activity: " + experimentId);

        // Bottom navigation
        BottomNavigationView mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationExperiment);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set menu item to People
        MenuItem navItem1 = mBottomNavigationView.getMenu().findItem(R.id.navigation_people);
        navItem1.setChecked(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Go directly to the People Fragment
        transaction.replace(R.id.content, new PeopleFragment()).commit();
    }

    public Map getExperimentDataFromParent() {
        return experimentData;
    }

    public String getExperimentIdFromParent() {
        return experimentId;
    }
}
