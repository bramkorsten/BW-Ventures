package com.hizmet.bluewhaleventures;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.hizmet.bluewhaleventures.fragments.ExperimentsFragment;
import com.hizmet.bluewhaleventures.fragments.InformationFragment;
import com.hizmet.bluewhaleventures.fragments.SettingsFragment;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    ExperimentsFragment experimentsFragment;
    InformationFragment informationFragment;
    SettingsFragment settingsFragment;
    Fragment currentFragment;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private String[] permissions = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(android.Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some permissions are denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int permission1 = PermissionChecker.checkSelfPermission(this, permissions[0]);
        int permission2 = PermissionChecker.checkSelfPermission(this, permissions[1]);
        if (permission1 == PermissionChecker.PERMISSION_GRANTED && permission2 == PermissionChecker.PERMISSION_GRANTED) {
            // Good to go, permissions already granted
        } else {
            // Permission not already granted, request permissions
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }

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
