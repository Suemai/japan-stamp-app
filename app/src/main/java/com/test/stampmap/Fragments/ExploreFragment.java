package com.test.stampmap.Fragments;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import com.test.stampmap.Activity.MainActivity;
import com.test.stampmap.Dialogues.FilterSheetDialogue;
import com.test.stampmap.Dialogues.StampSheetDialogue;
import com.test.stampmap.Filter.Filters;
import com.test.stampmap.Interface.IFilter;
import com.test.stampmap.R;
import com.test.stampmap.Settings.ConfigValue;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampMarker;
import com.test.stampmap.Stamp.StampSet;
import org.jetbrains.annotations.Nullable;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
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
    public static GpsMyLocationProvider locationProvider = null;
    public static float distanceSliderValue = 0;
    private SearchView searchBar;
    private AudioManager audioManager;
    private MapView map;
    private CompassOverlay compass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate and create a map
        map = new MapView(inflater.getContext());
        View v = inflater.inflate(R.layout.explore_fragment, container, false);
        ((RelativeLayout) v.findViewById(R.id.mapView)).addView(map);

        searchBar = v.findViewById(R.id.searchBar);
        searchBar.setQueryHint("KNOBHEAD");

        TextView searchText = searchBar.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_NULL) {
                closeKeyboard();
                String queryText = searchBar.getQuery().toString();
                return performSearch(TextUtils.getTrimmedLength(queryText) > 0 ? queryText : null);
            }
            return false;
        });

        searchBar.setOnClickListener(view -> searchBar.setIconified(false));

        ImageButton filterBottom = v.findViewById(R.id.filter);
        filterBottom.setOnClickListener(view -> {
            new FilterSheetDialogue().show(getChildFragmentManager(), "ModalBottomSheet");
            closeKeyboard();
        });

        audioManager = (AudioManager)requireContext().getSystemService(Context.AUDIO_SERVICE);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay);

        //location marker - dunno if it works, but there's something
        ExploreFragment.locationProvider = new GpsMyLocationProvider(requireContext());
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(ExploreFragment.locationProvider, map);
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        map.getController().setZoom(19.5);

        map.getOverlays().add(locationOverlay);

        //button to get center your current location
        ImageButton currentButton = v.findViewById(R.id.btn_passenger_current_location);
        currentButton.setOnClickListener(view -> {
            locationOverlay.enableFollowLocation();
            map.getController().setZoom(19.5);
            map.setMapOrientation(0);
        });
        currentButton.bringToFront();

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

        //adds compass (doesn't do compass work whilst rotating with map)
        // update: now we compassing
        compass = new CompassOverlay(requireContext(), new IOrientationProvider() {
            public boolean startOrientationProvider(IOrientationConsumer orientationConsumer) {
                return true;
            }
            public void stopOrientationProvider() {}
            public float getLastKnownOrientation() {
                return -map.getMapOrientation();
            }
            public void destroy() {}
        }, map);
        map.getOverlays().add(compass);
        compass.enableCompass();
        compass.onOrientationChanged(-map.getMapOrientation(), compass.getOrientationProvider());
        map.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                compass.onOrientationChanged(-map.getMapOrientation(), compass.getOrientationProvider());
                return false;
            }
            public boolean onZoom(ZoomEvent event) {return false;}
        });

        //button to reset rotation
        Button compassButton = v.findViewById(R.id.compassButton);
        compassButton.setOnClickListener(view -> {
            map.setMapOrientation(0);
            compass.onOrientationChanged(0, compass.getOrientationProvider());
        });

        //moving the compass location
        compass.setCompassCenter(40, v.findViewById(R.id.toolbar).getBackground().getMinimumHeight());

        loadMarkers();

        return v;
    }

    public boolean performSearch(@Nullable String query) {
        map.getOverlays().removeAll(map.getOverlays().stream().filter(item -> item instanceof Marker).collect(Collectors.toList()));
        List<IFilter> clone = (List<IFilter>) ((ArrayList<IFilter>) MainActivity.filters).clone();
        if (query != null) clone.add(Filters.SearchType.ANY.set(query));
        if (distanceSliderValue > 0) clone.add(Filters.Distance.KILOMETRES.set(distanceSliderValue));
        IFilter[] searchFilters = clone.toArray(new IFilter[0]);
        ArrayList<StampSet> filteredStamps = Filters.FilterStamps(searchFilters);
        loadMarkers();
        handleMapMovementAndZoom(filteredStamps);
        if (ConfigValue.CLEAR_FILTERS.getValue()) {
            MainActivity.filters.clear();
            distanceSliderValue = 0;
        }
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

    private void loadMarkers(){
        for (StampSet stampSet : StampCollection.getInstance().getCurrentFilteredStamps()) {
            StampMarker stampMarker = new StampMarker(map, stampSet);
            GeoPoint baseCoords = stampSet.getStamps().get(0).getCoordinates();
            stampMarker.setPosition(baseCoords);
            map.getOverlays().add(stampMarker);
//            stampMarker.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.person, getResources().newTheme()));
            stampMarker.setOnMarkerClickListener((marker, view) -> {
                new StampSheetDialogue(stampMarker.getStampSet()).show(getChildFragmentManager(), "ModalBottomSheet");
                audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
                return true;
            });
            map.getOverlays().remove(compass);
            map.getOverlays().add(compass);
            closeKeyboard();
        }
    }

    void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        TextView view = searchBar.findViewById(androidx.appcompat.R.id.search_src_text);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        searchBar.clearFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
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
}
