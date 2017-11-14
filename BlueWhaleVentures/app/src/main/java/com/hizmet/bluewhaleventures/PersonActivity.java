package com.hizmet.bluewhaleventures;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.hizmet.bluewhaleventures.fragments.ExperimentsFragment;
import com.hizmet.bluewhaleventures.fragments.InformationFragment;
import com.hizmet.bluewhaleventures.fragments.PersonFragment;
import com.hizmet.bluewhaleventures.fragments.QuestionsFragment;
import com.hizmet.bluewhaleventures.fragments.SettingsFragment;

import java.util.List;
import java.util.Map;

public class PersonActivity extends AppCompatActivity {
    private Map personData;
    private String personId, experimentId;
    private BottomNavigationView mBottomNavigationView;
    private LinearLayout contentView;

    QuestionsFragment questionsFragment;
    PersonFragment personFragment;
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

                case R.id.navigation_questions:

                    if (fragmentManager.findFragmentByTag(questionsFragment.getTag()) == null) {
                        transaction.add(R.id.content, questionsFragment, questionsFragment.getClass().getSimpleName());
                    }

                    transaction.hide(currentFragment);
                    transaction.show(questionsFragment);
                    transaction.commit();
                    currentFragment = questionsFragment;
                    return true;
                case R.id.navigation_persondetail:

                    if (fragmentManager.findFragmentByTag(personFragment.getTag()) == null) {
                        transaction.add(R.id.content, personFragment, personFragment.getClass().getSimpleName());
                    }

                    transaction.hide(currentFragment);
                    transaction.show(personFragment);
                    transaction.commit();
                    currentFragment = personFragment;
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

            List<Fragment> list =  fragmentManager.getFragments();
            int i = 0;
            while ( i < list.size()) {
                Log.d("ventures", list.get(i).getTag());
                i++;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        contentView = findViewById(R.id.content);

        Intent intent = getIntent();
        personData = (Map) intent.getSerializableExtra("map");
        personId = intent.getStringExtra("id");
        experimentId = intent.getStringExtra("experimentId");

        // Bottom navigation
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationPerson);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem navItem2 = mBottomNavigationView.getMenu().findItem(R.id.navigation_questions);
        navItem2.setChecked(true);

        questionsFragment = QuestionsFragment.newInstance("", "");
        personFragment = PersonFragment.newInstance("", "");
        settingsFragment = new SettingsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Go to the Questions Fragment
        transaction.add(R.id.content, questionsFragment, questionsFragment.getClass().getSimpleName()).show(questionsFragment).commit();
        currentFragment = questionsFragment;
    }

    public Map getPersonDataFromParent() {
        return personData;
    }

    public String getPersonIdFromParent() {
        return personId;
    }

    public String getExperimentIdFromParent() {
        return experimentId;
    }
}
