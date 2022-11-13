package com.test.stampmap.Views;

import android.content.Context;
import android.util.Log;
import com.test.stampmap.Fragments.ExploreFragment;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapViewRepository;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.IOrientationConsumer;
import org.osmdroid.views.overlay.compass.IOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class CustomMapView extends MapView {
    public CompassOverlay compass;
    private MapViewRepository repo;
    public MyLocationNewOverlay locationOverlay;
    public CustomMapView(Context context) {
        super(context);
        setupMap(context);
        repo = getRepository();
    }

    public void setupMap(Context context){
        //allow finger movement......it's not what ur thinking....
        //ZOOM man ZOOM
        this.setMultiTouchControls(true);
        Log.i("PLEASE DON'T", "DO THIS TO ME");

        //allow rotating movement
        RotationGestureOverlay rotationOverlay = new RotationGestureOverlay(this);
        rotationOverlay.setEnabled(true);
        this.setMultiTouchControls(true);
        this.getOverlays().add(rotationOverlay);

        //location marker - dunno if it works, but there's something
        ExploreFragment.locationProvider = new GpsMyLocationProvider(context);
        locationOverlay = new MyLocationNewOverlay(ExploreFragment.locationProvider, this);
        locationOverlay.enableMyLocation();
        this.getOverlays().add(locationOverlay);

        //remove zoom buttons
        this.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        //adds compass (doesn't do compass work whilst rotating with map)
        // update: now we compassing
        compass = new CompassOverlay(context, new IOrientationProvider() {
            public boolean startOrientationProvider(IOrientationConsumer orientationConsumer) {
                return true;
            }
            public void stopOrientationProvider() {
            }
            public float getLastKnownOrientation() {
                return 0;
            }
            public void destroy() {
            }
        }, this);
        this.getOverlays().add(compass);
        compass.enableCompass();
        compass.onOrientationChanged(-this.getMapOrientation(), compass.getOrientationProvider());
        setOnTouchListener((view, event) -> {
            view.performClick();
            compass.onOrientationChanged(-this.getMapOrientation(), compass.getOrientationProvider());
            return false;
        });
    }

    @Override
    public MapViewRepository getRepository() {
        if (repo == null) repo = new MapViewRepository(this);
        return repo;
    }
}
