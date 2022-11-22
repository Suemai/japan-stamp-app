package com.test.stampmap.Activity;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.test.stampmap.Dialogues.FilterSheetDialogue;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.test.stampmap.Dialogues.StampSheetDialogue;
import com.test.stampmap.Filter.Filters;
import com.test.stampmap.Fragments.ExploreFragment;
import com.test.stampmap.Fragments.MyStampsFragment;
import com.test.stampmap.Fragments.SettingsFragment;
import com.test.stampmap.Interface.IFilter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampMarker;
import com.test.stampmap.Stamp.StampSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.Distance;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.IOrientationConsumer;
import org.osmdroid.views.overlay.compass.IOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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

        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,exploreFrag).commit();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            switch(item.getItemId()){

                case R.id.myStamps:
                    fragment = myStampsFrag;
                    break;

                case R.id.settings:
                    fragment = settingsFrag;
                    break;

                default:
                    fragment = exploreFrag;
                    break;
            }
            navFragmentsManager.beginTransaction().replace(R.id.mainContainer, fragment).commit();
            return true;
        });
    }
}