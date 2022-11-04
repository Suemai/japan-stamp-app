package com.test.stampmap;

import Dialogues.BottomSheetDialogue;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.jetbrains.annotations.Nullable;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements MapEventsReceiver {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private SearchView searchBar = null;
    private final String STAMP_FILE = "all-stamps-coords.json";
    private final BottomSheetDialogue filterSheet = new BottomSheetDialogue();
    public static List<IFilter> filters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handle permissions first
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //get current location
                Manifest.permission.ACCESS_FINE_LOCATION,
        });

        //inflate and create a map
        setContentView(R.layout.activity_main);

        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //allow finger movement......it's not what ur thinking....
        //ZOOM man ZOOM
        map.setMultiTouchControls(true);


        //allow rotating movement
        RotationGestureOverlay rotationOverlay = new RotationGestureOverlay(this, map);
        rotationOverlay.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(rotationOverlay);

        //remove zoom buttons
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        //set the starting point
        IMapController mapController = map.getController();
        mapController.setZoom(19.0);


        GeoPoint startPoint = new GeoPoint(36.117052, 140.098735); //my dorm bitch
        //Update: redundant line of code
        //mapController.setCenter(startPoint);


        //location marker - dunno if it works, but there's something
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        map.getOverlays().add(locationOverlay);


        //button to get center your current location
        ImageButton currentButton = findViewById(R.id.btn_passenger_current_location);
        currentButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                locationOverlay.enableFollowLocation();
                mapController.setZoom(19.5);
                map.setMapOrientation(0);
                return false;
            }
        });


        //add marker to starting point
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        startMarker.setTitle("Ichinoya Building 35");

        map.getOverlays().add(startMarker);


        //marker off on short press
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        map.getOverlays().add(0, mapEventsOverlay);


        //adds compass (doesn't do compass work whilst rotating with map)
        // update: now we compassing
        CompassOverlay compass = new CompassOverlay(this, new IOrientationProvider() {
            public boolean startOrientationProvider(IOrientationConsumer orientationConsumer) {
                return true;
            }

            public void stopOrientationProvider() {
            }

            public float getLastKnownOrientation() {
                return -map.getMapOrientation();
            }

            public void destroy() {
            }
        }, map);
        map.getOverlays().add(compass);
        compass.enableCompass();
        compass.onOrientationChanged(-map.getMapOrientation(), compass.getOrientationProvider());
        map.setOnTouchListener((v, event) -> {
            v.performClick();
            compass.onOrientationChanged(-map.getMapOrientation(), compass.getOrientationProvider());
            return false;
        });

        //compass on rotate on touch
        Button compassButton = findViewById(R.id.compassButton);
        compassButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                map.setMapOrientation(0);
                compass.onOrientationChanged(0, compass.getOrientationProvider());
                return false;
            }
        });

        //moving the compass location
        compass.setCompassCenter(40, findViewById(R.id.toolbar).getBackground().getMinimumHeight());

        searchBar = findViewById(R.id.searchBar);
        searchBar.setQueryHint("KNOBHEAD");

        TextView searchText = searchBar.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER){
                if (searchBar.getQuery().toString().equals("")){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return performSearch(null);
                }
            }
            return false;
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return performSearch(query);}
            @Override
            public boolean onQueryTextChange(String newText) {return false;}
        });

        ImageButton filterBottom = findViewById(R.id.filter);
        filterBottom.setOnClickListener(view -> filterSheet.show(getSupportFragmentManager(), "ModalBottomSheet"));

        // distance filter do something about it
        //commented out cus it crashes it for some reason
//        SeekBar distanceFilter = findViewById(R.id.distance_slider);
//        distanceFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

    }

    public boolean performSearch(@Nullable String query){
        map.getOverlays().removeAll(map.getOverlays().stream().filter(item -> item instanceof Marker).collect(Collectors.toList()));
        JSONArray parsedJson = loadJSONArrayFromAsset(STAMP_FILE);
        List<IFilter> clone = (List<IFilter>)((ArrayList<IFilter>) filters).clone();
        if(query!= null) clone.add(Filters.SearchType.ANY);
        IFilter[] searchFilters = clone.toArray(new IFilter[0]);
        ArrayList<StampSet> filteredStamps = Filters.FilterStamps(parsedJson, searchFilters, query);

        for (StampSet stampSet : filteredStamps) {
            Marker stampMarker = new Marker(map);
            GeoPoint baseCoords = stampSet.getStamps().get(0).getCoordinates();
            stampMarker.setTitle(stampSet.toString());
            stampMarker.setPosition(baseCoords);
            map.getOverlays().add(stampMarker);
            stampMarker.setOnMarkerClickListener((marker, mapView) -> {
                if (marker.isInfoWindowShown()) marker.closeInfoWindow();
                else{
                    mapView.getController().animateTo(marker.getPosition());
                    marker.showInfoWindow();
                }
                return true;
            });
        }
        return false;
    }

    @Override
    public void onResume(){
        super.onResume();
        map.onResume();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        //locationOverlay.enableMyLocation();
    }

    @Override
    public void onPause(){
        super.onPause();
        map.onPause();
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

    };

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(this, permission)
                    !=PackageManager.PERMISSION_GRANTED){
                permissionsToRequest.add(permission);

            }
        }
        if(permissionsToRequest.size() > 0){
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        InfoWindow.closeAllInfoWindowsOn(map);
        searchBar.clearFocus();
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        //DO NOTHING FOR NOW:
        return false;
    }

    //search bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_options, menu);
        return true;
    }

    /**
     * Thank you StackOverflow
     *
     * @param fileName file to be loaded from assets
     * @return {@link String} of JSON data from file.
     */
    public JSONArray loadJSONArrayFromAsset(String fileName) {
        String json;
        try {
            InputStream is = this.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        JSONArray parsed = null;
        try {
            parsed = new JSONArray(json);
        } catch (JSONException ignored) {}
        return parsed;
    }
}