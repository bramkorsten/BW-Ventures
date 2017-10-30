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

import com.hizmet.bluewhaleventures.fragments.PersonFragment;
import com.hizmet.bluewhaleventures.fragments.QuestionsFragment;
import com.hizmet.bluewhaleventures.fragments.SettingsFragment;

import java.util.Map;

public class PersonActivity extends AppCompatActivity {
    private Map personData;
    private String personId;

    // Bottom navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_questions:
                    // Go to the Questions Fragment
                    transaction.replace(R.id.content, new QuestionsFragment()).commit();
                    return true;
                case R.id.navigation_persondetail:
                    // Go to the Person Fragment
                    transaction.replace(R.id.content, new PersonFragment()).commit();
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
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();
        personData = (Map) intent.getSerializableExtra("map");
        personId = intent.getStringExtra("id");
        Log.d("ventures", "personID in activity: " + personId);

        // Bottom navigation
        BottomNavigationView mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationPerson);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem navItem2 = mBottomNavigationView.getMenu().findItem(R.id.navigation_persondetail);
        navItem2.setChecked(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Go to the Person Fragment
        transaction.replace(R.id.content, new PersonFragment()).commit();
    }

    public Map getPersonDataFromParent() {
        return personData;
    }

    public String getPersonIdFromParent() {
        return personId;
    }
}
