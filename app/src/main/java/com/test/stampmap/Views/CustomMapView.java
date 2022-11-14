package com.test.stampmap.Views;

import android.content.Context;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapViewRepository;

public class CustomMapView extends MapView {
    private MapViewRepository repo;

    public CustomMapView(Context context) {
        super(context);
        repo = getRepository();
    }

    @Override
    public MapViewRepository getRepository() {
        if (repo == null) repo = new MapViewRepository(this);
        return repo;
    }
}
