package com.test.stampmap.Fragments;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.widget.SearchView;
import com.test.stampmap.Activity.MainActivity;
import com.test.stampmap.Dialogues.FilterSheetDialogue;
import com.test.stampmap.Dialogues.StampSheetDialogue;
import com.test.stampmap.Filter.Filters;
import com.test.stampmap.Interface.IFilter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampMarker;
import com.test.stampmap.Stamp.StampSet;
import com.test.stampmap.Views.CustomMapView;
import org.jetbrains.annotations.Nullable;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.Distance;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExploreFragment extends BaseMapFragment implements MapEventsReceiver {
    private SearchView searchBar = null;
    public static GpsMyLocationProvider locationProvider = null;
    public static float distanceSliderValue = 0;
    public View v = null;
    AudioManager audioManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate and create a map
        mMapView = ((CustomMapView)super.onCreateView(inflater, container, savedInstanceState));
        v = inflater.inflate(R.layout.explore_fragment, container, false);
        ((RelativeLayout) v.findViewById(R.id.mapView)).addView(mMapView);

        searchBar = v.findViewById(R.id.searchBar);
        searchBar.setQueryHint("KNOBHEAD");

        TextView searchText = searchBar.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_NULL) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String queryText = searchBar.getQuery().toString();
                return performSearch(TextUtils.getTrimmedLength(queryText) > 0 ? queryText : null);
            }
            return false;
        });

        ImageButton filterBottom = v.findViewById(R.id.filter);
        filterBottom.setOnClickListener(view -> new FilterSheetDialogue().show(getChildFragmentManager(), "ModalBottomSheet"));

        audioManager = (AudioManager)requireContext().getSystemService(Context.AUDIO_SERVICE);

        return v;
    }

    public boolean performSearch(@Nullable String query) {
        mMapView.getOverlays().removeAll(mMapView.getOverlays().stream().filter(item -> item instanceof Marker).collect(Collectors.toList()));
        List<IFilter> clone = (List<IFilter>) ((ArrayList<IFilter>) MainActivity.filters).clone();
        if (query != null) clone.add(Filters.SearchType.ANY.set(query));
        if (distanceSliderValue > 0) clone.add(Filters.Distance.KILOMETRES.set(distanceSliderValue));
        IFilter[] searchFilters = clone.toArray(new IFilter[0]);
        ArrayList<StampSet> filteredStamps = Filters.FilterStamps(searchFilters);
        loadMarkers();
        handleMapMovementAndZoom(filteredStamps);
        MainActivity.filters.clear();
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
        if (trueExtremes.length == 1) mMapView.getController().setZoom(18.0f);
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
        if (coorDistance > 0) mMapView.getController().setZoom(zoom + 1.6);
        mMapView.getController().animateTo(new GeoPoint(coords[0] / 2, coords[1] / 2));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMapData();
    }

    private void loadMapData(){
        //marker off on short press
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        mMapView.getOverlays().add(0, mapEventsOverlay);

        loadMarkers();

        //button to get center your current location
        ImageButton currentButton = v.findViewById(R.id.btn_passenger_current_location);
        currentButton.setOnClickListener(view -> {
            mMapView.locationOverlay.enableFollowLocation();
            mMapView.getController().setZoom(19.5);
            mMapView.setMapOrientation(0);
        });
        currentButton.bringToFront();

        //button to reset rotation
        Button compassButton = v.findViewById(R.id.compassButton);
        compassButton.setOnClickListener(view -> {
            mMapView.setMapOrientation(0);
            mMapView.compass.onOrientationChanged(0, mMapView.compass.getOrientationProvider());
        });

        //moving the compass location
        mMapView.compass.setCompassCenter(40, v.findViewById(R.id.toolbar).getBackground().getMinimumHeight());
    }

    private void loadMarkers(){
        for (StampSet stampSet : StampCollection.getInstance().getCurrentFilteredStamps()) {
            StampMarker stampMarker = new StampMarker(mMapView, stampSet);
            GeoPoint baseCoords = stampSet.getStamps().get(0).getCoordinates();
            stampMarker.setPosition(baseCoords);
            mMapView.getOverlays().add(stampMarker);
            stampMarker.setOnMarkerClickListener((marker, view) -> {
                new StampSheetDialogue(stampMarker.getStampSet()).show(getChildFragmentManager(), "ModalBottomSheet");
                audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
                return true;
            });
            mMapView.getOverlays().remove(mMapView.compass);
            mMapView.getOverlays().add(mMapView.compass);
        }
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        InfoWindow.closeAllInfoWindowsOn(mMapView);
        searchBar.clearFocus();
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        //DO NOTHING FOR NOW:
        return false;
    }
}
