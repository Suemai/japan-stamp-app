package com.test.stampmap.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.test.stampmap.Settings.ConfigValue;
import com.test.stampmap.Settings.UserSettings;
import com.test.stampmap.Stamp.StampCollection;
import org.osmdroid.config.Configuration;

import java.lang.reflect.Constructor;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    public static List<IFilter> filters = new ArrayList<>();
    Fragment currentFragment;
    public static int paddedStatusBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserSettings.setUserSettings((UserSettings) getApplication());
        loadSharedPreferences();

        //handle permissions first
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                //get current location
                Manifest.permission.ACCESS_FINE_LOCATION,
        });

        //load stamp data
        StampCollection.getInstance().load(getApplication());

        //inflate and create a map
        setContentView(R.layout.activity_main);

        addBottomNavigation();
        UserSettings.setStatusBarUI(this, false);
        int density = getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        paddedStatusBarHeight = UserSettings.getStatusBarHeight() + density * 8;
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
            int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public void addBottomNavigation(){
        Fragment exploreFrag = getFragment(ExploreFragment.class);
        Fragment myStampsFrag = getFragment(MyStampsFragment.class);
        Fragment settingsFrag = getFragment(SettingsFragment.class);

        if (getSupportFragmentManager().getFragments().isEmpty()){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainContainer, exploreFrag)
                    .add(R.id.mainContainer, myStampsFrag).hide(myStampsFrag)
                    .add(R.id.mainContainer, settingsFrag).hide(settingsFrag)
                    .commit();
            currentFragment = exploreFrag;
            getSupportFragmentManager().executePendingTransactions();
        }
        else currentFragment = getShownFragment();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {

            int[] leftAnimation = {R.anim.exit_to_left, R.anim.enter_from_right};
            int[] rightAnimation = {R.anim.exit_to_right, R.anim.enter_from_left};

            switch(item.getItemId()){

                case R.id.myStamps:
                    int[] animation = currentFragment == exploreFrag ? leftAnimation : rightAnimation;
                    performTransaction(myStampsFrag, animation);
                    break;

                case R.id.settings:
                    performTransaction(settingsFrag, leftAnimation);
                    break;

                default:
                    performTransaction(exploreFrag, rightAnimation);
                    break;
            }
            return true;
        });
    }

    void performTransaction(Fragment fragment, int[] animation){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(animation[0], animation[0])
                .hide(currentFragment)
                .setCustomAnimations(animation[1], animation[1])
                .show(fragment)
                .commit();
        currentFragment.onPause();
        currentFragment = fragment;
        getSupportFragmentManager().executePendingTransactions();
        fragment.onResume();
    }

    void loadSharedPreferences(){
        for (ConfigValue configValue : ConfigValue.values()) configValue.getValue();
    }

    @NonNull Fragment getFragment(Class<? extends Fragment> klass) throws NullPointerException {
        for (Fragment fragment : getSupportFragmentManager().getFragments()){
            if (fragment.getClass() == klass) return fragment;
        }
        try{ return klass.newInstance(); }
        catch (Exception ignore){}
        throw new NullPointerException("Unable to create Fragment");
    }

    Fragment getShownFragment(){
        return getSupportFragmentManager().getFragments()
                .stream()
                .filter(fragment -> !fragment.isHidden())
                .findFirst()
                .orElse(null);

    }
}