package com.test.stampmap.Activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.test.stampmap.Interface.IFilter;
import com.test.stampmap.R;
import com.test.stampmap.Settings.ConfigValue;
import com.test.stampmap.Settings.SupportedLocale;
import com.test.stampmap.Settings.UpdateManager;
import com.test.stampmap.Settings.UserSettings;
import com.test.stampmap.Stamp.StampCollection;
import org.jetbrains.annotations.NotNull;
import org.osmdroid.config.Configuration;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    public static List<IFilter> filters = new ArrayList<>();
    Fragment currentFragment;
    public static int paddedStatusBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserSettings.setUserSettings((UserSettings) getApplication());
        loadSharedPreferences();
        Log.i("DEFAULT LOCALE", Locale.getDefault().getDisplayLanguage());
        setLocale(this, SupportedLocale.getCurrent(), true);
        super.onCreate(savedInstanceState);

        //handle permissions first
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                //get current location
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,

                Manifest.permission.REQUEST_INSTALL_PACKAGES,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET
        });

        // Check for updates when app opens
        UpdateManager updateManager = new UpdateManager(this);
        updateManager.checkForUpdates(false);

        //load stamp data
        StampCollection.getInstance().load(getApplication());

        //inflate and create a map
        setContentView(R.layout.activity_main);

        bottomNavigation();
        //addBottomNavigation();
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

    public void bottomNavigation(){
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNav, navController);

    }

    void loadSharedPreferences(){
        for (ConfigValue configValue : ConfigValue.values()) configValue.getValue();
    }

    public static void setLocale(Activity activity, SupportedLocale lang){
        setLocale(activity, lang, false);
    }
    private static void setLocale(Activity activity, SupportedLocale lang, boolean firstActivation) {
        ConfigValue.APP_LOCALE.setValue(lang.name());
        Locale locale;
        Log.i("my locale", Locale.getDefault().toString());
        if (lang == SupportedLocale.DEFAULT) locale = SupportedLocale.getSupportedLocaleFor(Locale.getDefault()).getLocale();
        else locale = lang.getLocale();

        if (firstActivation && Locale.getDefault().equals(locale)) return;
        Log.i("MainActivity.setLocale", String.format("changing language to %s!", lang.getName()));
        Resources resources = activity.getResources();
        resources.getConfiguration().setLocale(locale);
        resources.updateConfiguration(resources.getConfiguration(), resources.getDisplayMetrics());
        if (!firstActivation) activity.onConfigurationChanged(resources.getConfiguration());
    }

    @Override
    public void onConfigurationChanged(@NotNull android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("MainActivity.onConfigurationChanged", "redrawing navigation bar");
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        View help = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_main, null);
        View newNav = help.findViewById(R.id.bottomNavigationView);
        ((RelativeLayout)help.findViewById(R.id.mainContainer)).removeAllViews();
        RelativeLayout layout = findViewById(R.id.mainContainer);
        layout.removeView(bottomNav);
        layout.addView(newNav, 0);
        bottomNavigation();
    }
}