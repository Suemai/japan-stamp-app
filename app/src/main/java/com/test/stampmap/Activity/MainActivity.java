package com.test.stampmap.Activity;

import android.content.SharedPreferences;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.test.stampmap.Adapter.NavigationBarAdapter;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.test.stampmap.Fragments.ExploreFragment;
import com.test.stampmap.Fragments.MyStampsFragment;
import com.test.stampmap.Fragments.SettingsFragment;
import com.test.stampmap.Fragments.WishlistFragment;
import com.test.stampmap.Interface.IFilter;
import com.test.stampmap.R;
import com.test.stampmap.Settings.ConfigValue;
import com.test.stampmap.Settings.UserSettings;
import com.test.stampmap.Stamp.StampCollection;
import org.jetbrains.annotations.NotNull;
import org.osmdroid.config.Configuration;

import java.util.*;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    public static List<IFilter> filters = new ArrayList<>();
    public static float distanceSliderValue = 0;

    ViewPager2 viewPager2;
    NavigationBarAdapter mainFragmentsViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserSettings.setUserSettings((UserSettings) getApplication());

        //handle permissions first
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //get current location
                Manifest.permission.ACCESS_FINE_LOCATION,
        });

        //load stamp data
        StampCollection.getInstance().load(ctx);

        //inflate and create a map
        setContentView(R.layout.activity_main);
        List<Fragment> navigationFragments = Arrays.stream(new Fragment[]{new ExploreFragment(), new MyStampsFragment(), new SettingsFragment()}).collect(Collectors.toList());
        viewPager2 = findViewById(R.id.mainFragmentViewer);
        mainFragmentsViewAdapter = new NavigationBarAdapter(getSupportFragmentManager(), getLifecycle(), navigationFragments);
        viewPager2.setAdapter(mainFragmentsViewAdapter);
        viewPager2.setUserInputEnabled(false);

        //bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.explore);

        //perform listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.explore:
                    viewPager2.setCurrentItem(0);
                    return true;

                case R.id.myStamps:
                    viewPager2.setCurrentItem(1);
                    return true;

                case R.id.settings:
                    viewPager2.setCurrentItem(2);
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);

            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    void loadSharedPreferences(){
        for (ConfigValue configValue : ConfigValue.values()) configValue.getValue();
    }
}