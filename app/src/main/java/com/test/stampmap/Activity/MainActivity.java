package com.test.stampmap.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.test.stampmap.Fragments.ExploreFragment;
import com.test.stampmap.Fragments.MyStampsFragment;
import com.test.stampmap.Fragments.SettingsFragment;
import com.test.stampmap.Interface.IFilter;
import com.test.stampmap.R;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    public static List<IFilter> filters = new ArrayList<>();
    public static GpsMyLocationProvider locationProvider = null;
    public static float distanceSliderValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //get current location
                Manifest.permission.ACCESS_FINE_LOCATION,
        });

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        addBottomNavigation();

    }

    @Override
    public void onResume() {
        super.onResume();
        //locationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        //locationOverlay.disableMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }

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

    public void addBottomNavigation(){

        Fragment exploreFrag = new ExploreFragment();
        Fragment myStampsFrag = new MyStampsFragment();
        Fragment settingsFrag = new SettingsFragment();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        final FragmentManager navFragmentsManager = getSupportFragmentManager();

        navFragmentsManager.beginTransaction()
                .add(R.id.mainContainer, exploreFrag)
                .add(R.id.mainContainer, myStampsFrag).hide(myStampsFrag)
                .add(R.id.mainContainer, settingsFrag).hide(settingsFrag)
                .commit();

        AtomicReference<Fragment> fragment = new AtomicReference<>(exploreFrag);

        bottomNav.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){

                case R.id.myStamps:
                    int[] anims = fragment.get() instanceof ExploreFragment ?
                            new int[]{R.anim.exit_to_left, R.anim.enter_from_right} :
                            new int[]{R.anim.exit_to_right, R.anim.enter_from_left};
                    navFragmentsManager.beginTransaction()
                            .setCustomAnimations(anims[0], anims[0])
                            .hide(fragment.get())
                            .setCustomAnimations(anims[1], anims[1])
                            .show(myStampsFrag).commit();
                    fragment.set(myStampsFrag);
                    break;

                case R.id.settings:
                    navFragmentsManager.beginTransaction()
                            .setCustomAnimations(R.anim.exit_to_left, R.anim.exit_to_left)
                            .hide(fragment.get())
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.enter_from_right)
                            .show(settingsFrag).commit();
                    fragment.set(settingsFrag);

                    break;

                default:
                    navFragmentsManager.beginTransaction()
                            .setCustomAnimations(R.anim.exit_to_right, R.anim.exit_to_right)
                            .hide(fragment.get())
                            .setCustomAnimations(R.anim.enter_from_left, R.anim.enter_from_left)
                            .show(exploreFrag).commit();
                    fragment.set(exploreFrag);
                    break;
            }
            return true;
        });
    }
}