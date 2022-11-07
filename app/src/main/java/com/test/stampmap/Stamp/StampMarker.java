package com.test.stampmap.Stamp;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class StampMarker extends Marker {

    private final StampSet stampSet;

    public StampMarker(MapView mapView, StampSet stampSet) {
        super(mapView);
        this.stampSet = stampSet;
    }

    public final StampSet getStampSet(){
        return this.stampSet;
    }

}
