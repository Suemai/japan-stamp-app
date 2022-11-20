package com.test.stampmap.Fragments;

import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.test.stampmap.R;
import com.test.stampmap.Settings.ConfigValue;

import java.util.concurrent.Executors;


public class SettingsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.settings_fragment, container, false);
        SwitchCompat clearFilters = v.findViewById(R.id.filterClear);
        clearFilters.setChecked(ConfigValue.CLEAR_FILTERS.getValue());
        clearFilters.setOnCheckedChangeListener((buttonView, isChecked) -> ConfigValue.CLEAR_FILTERS.setValue(isChecked));
        v.findViewById(R.id.cache_clear).setOnClickListener(v1 -> Executors.newSingleThreadExecutor().execute(() -> Glide.get(requireContext()).clearDiskCache()));
       return v;
    }
}