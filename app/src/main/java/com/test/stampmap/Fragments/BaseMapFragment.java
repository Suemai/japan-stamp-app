package com.test.stampmap.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import androidx.fragment.app.Fragment;
import com.test.stampmap.Views.CustomMapView;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

public abstract class BaseMapFragment extends Fragment {
    public static final String TAG = "osmBaseFrag";
    protected CustomMapView mMapView;
    boolean firstActivation = true;

    public CustomMapView getmMapView() {
        return mMapView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMapView = new CustomMapView(inflater.getContext());
        Log.d(TAG, "onCreateView");
        return mMapView;
    }

    @Override
    public void onPause() {
        if (mMapView != null) mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) mMapView.onResume();
        getmMapView().setTileProvider(new MapTileProviderBasic(requireContext(), TileSourceFactory.MAPNIK));
        if (!firstActivation) mMapView.setupMap(requireContext());
        else {
            mMapView.locationOverlay.enableFollowLocation();
            mMapView.getController().setZoom(19.5);
        }
        firstActivation = false;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDetach");
        if (mMapView != null)
            mMapView.onDetach();
        mMapView = null;
        super.onDestroyView();
    }
}
