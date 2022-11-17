package com.test.stampmap.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import com.test.stampmap.Dialogues.StampSheetDialogue;
import com.test.stampmap.Filter.Filters;
import com.test.stampmap.Interface.IFilter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampMarker;
import com.test.stampmap.Stamp.StampSet;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ExploreFragment extends Fragment implements MapEventsReceiver {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private SearchView searchBar = null;
    public static List<IFilter> filters = new ArrayList<>();
    public static GpsMyLocationProvider locationProvider = null;
    public static float distanceSliderValue = 0;
    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ctx = getContext().getApplicationContext();

        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        //load stamp data
        StampCollection.getInstance().load(ctx);

        map = view.findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //allow finger movement......it's not what ur thinking....
        //ZOOM man ZOOM
        map.setMultiTouchControls(true);


        //allow rotating movement
        RotationGestureOverlay rotationOverlay = new RotationGestureOverlay(map);
        rotationOverlay.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(rotationOverlay);

        //remove zoom buttons
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        //set the starting point
        IMapController mapController = map.getController();
        mapController.setZoom(19.0);

        /*
        //not needed anymore
        //GeoPoint startPoint = new GeoPoint(36.117052, 140.098735); //my dorm bitch
        //Update: redundant line of code
        //mapController.setCenter(startPoint);
        */

        //location marker - dunno if it works, but there's something
        locationProvider = new GpsMyLocationProvider(ctx);
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(locationProvider, map);
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        map.getOverlays().add(locationOverlay);


        //button to get center your current location
        ImageButton currentButton = view.findViewById(R.id.btn_passenger_current_location);
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
        /*
        //update: not needed anymore
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        startMarker.setTitle("Ichinoya Building 35");

        map.getOverlays().add(startMarker);
        */

        //marker off on short press
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay);


        //adds compass (doesn't do compass work whilst rotating with map)
        // update: now we compassing
        CompassOverlay compass = new CompassOverlay(ctx, new IOrientationProvider() {
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
        Button compassButton = view.findViewById(R.id.compassButton);
        compassButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                map.setMapOrientation(0);
                compass.onOrientationChanged(0, compass.getOrientationProvider());
                return false;
            }
        });

        //moving the compass location
        compass.setCompassCenter(40, view.findViewById(R.id.toolbar).getBackground().getMinimumHeight());

        searchBar = view.findViewById(R.id.searchBar);
        searchBar.setQueryHint("KNOBHEAD");

        TextView searchText = searchBar.findViewById(androidx.appcompat.R.id.search_src_text);

        searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_NULL) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                String queryText = searchBar.getQuery().toString();
                return performSearch(TextUtils.getTrimmedLength(queryText) > 0 ? queryText : null);
            }
            return false;
        });

        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    public boolean performSearch(@Nullable String query) {
        map.getOverlays().removeAll(map.getOverlays().stream().filter(item -> item instanceof Marker).collect(Collectors.toList()));
        List<IFilter> clone = (List<IFilter>) ((ArrayList<IFilter>) filters).clone();
        if (query != null) clone.add(Filters.SearchType.ANY.set(query));
        if (distanceSliderValue > 0) clone.add(Filters.Distance.KILOMETRES.set(distanceSliderValue));
        IFilter[] searchFilters = clone.toArray(new IFilter[0]);
        ArrayList<StampSet> filteredStamps = Filters.FilterStamps(searchFilters);

        for (StampSet stampSet : filteredStamps) {
            StampMarker stampMarker = new StampMarker(map, stampSet);
            GeoPoint baseCoords = stampSet.getStamps().get(0).getCoordinates();
            stampMarker.setPosition(baseCoords);
            map.getOverlays().add(stampMarker);
            stampMarker.setOnMarkerClickListener((marker, view) -> {
                new StampSheetDialogue(stampMarker.getStampSet()).show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
                return true;
            });
        }
        handleMapMovementAndZoom(filteredStamps);
        filters.clear();
        distanceSliderValue = 0;
        return false;
    }

    void handleMapMovementAndZoom(ArrayList<StampSet> filteredStamps) {
        if (filteredStamps.isEmpty()) return;
        float[] coords = {0, 0};
        GeoPoint[] extremes = {null, null, null, null};
        for (StampSet stampSet : filteredStamps) {
            GeoPoint baseCoords = stampSet.getStamps().get(0).getCoordinates();
            if (baseCoords.getLongitude() == 0.0f || baseCoords.getLatitude() >= 90.0f) continue;
            if (extremes[0] == null || baseCoords.getLatitude() > extremes[0].getLatitude()) extremes[0] = baseCoords;
            if (extremes[1] == null || baseCoords.getLongitude() > extremes[1].getLongitude()) extremes[1] = baseCoords;
            if (extremes[2] == null || baseCoords.getLatitude() < extremes[2].getLatitude()) extremes[2] = baseCoords;
            if (extremes[3] == null || baseCoords.getLongitude() < extremes[3].getLongitude()) extremes[3] = baseCoords;
        }
        coords[0] += extremes[0].getLatitude() + extremes[2].getLatitude();
        coords[1] += extremes[1].getLongitude() + extremes[3].getLongitude();
        GeoPoint[] trueExtremes = Arrays.stream(extremes).distinct().toArray(GeoPoint[]::new);
        double coorDistance = 0;
        if (trueExtremes.length == 1) map.getController().setZoom(18.0f);
        else for (int i = 0; i < trueExtremes.length; i++) {
            for (int k = i + 1; k < trueExtremes.length; k++) {
                if (trueExtremes[i] != trueExtremes[k]) {
                    double dist = Math.sqrt(Distance.getSquaredDistanceToPoint(
                            trueExtremes[i].getLatitude(), trueExtremes[i].getLongitude(),
                            trueExtremes[k].getLatitude(), trueExtremes[k].getLongitude()));
                    if (dist > coorDistance) coorDistance = dist;
                }
            }
        }
        double zoom = Math.log(360 / coorDistance) / Math.log(2);
        if (coorDistance > 0) map.getController().setZoom(zoom + 1.6);
        map.getController().animateTo(new GeoPoint(coords[0] / 2, coords[1] / 2));
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        //InfoWindow.closeAllInfoWindowsOn(map);
        //searchBar.clearFocus();
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        //DO NOTHING FOR NOW:
        return false;
    }

    @Override
    public void onPause() {
        if (map != null) {
            map.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
        }
    }

    //search bar
    //@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.search_options, menu);
        return true;
    }
}
