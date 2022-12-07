package com.test.stampmap.Settings;

import org.osmdroid.util.GeoPoint;

public class MapState {
    public GeoPoint lastPosition;
    public double lastZoom;
    public float lastOrientation;
    public boolean searchIsIconified;
    public boolean searchHasFocus;
    public String searchText;

    public MapState(){
        lastPosition = null;
        lastZoom = -1;
        lastOrientation = 0;
        searchIsIconified = true;
    }
    public void saveState(GeoPoint position, double zoom, float orientation, boolean isIconified, boolean hasFocus, String searchText){
        lastPosition = position;
        lastZoom = zoom;
        lastOrientation = orientation;
        searchIsIconified = isIconified;
        searchHasFocus = hasFocus;
        this.searchText = searchText;
    }
}
