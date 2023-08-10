package com.test.stampmap.Fragments;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.test.stampmap.Activity.MainActivity;
import com.test.stampmap.R;
import com.test.stampmap.Settings.ConfigValue;
import com.test.stampmap.Settings.UpdateManager;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.Executors;


public class SettingsFragment extends Fragment {

    FrameLayout settingsFrag;
    LinearLayout settingsLayout;

    TextView about, help, updates;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.settings_fragment, container, false);

        //get fragment container layout
        settingsFrag = v.findViewById(R.id.settings_frag_container);
        settingsLayout = v.findViewById(R.id.settings_layout);


        //filters settings bit.... i think....
        v.findViewById(R.id.settings_layout).setPadding(v.getPaddingLeft(), MainActivity.paddedStatusBarHeight, v.getPaddingRight(), v.getPaddingBottom());
        SwitchCompat clearFilters = v.findViewById(R.id.filterClear);
        clearFilters.setChecked(ConfigValue.CLEAR_FILTERS.getValue());
        clearFilters.setOnCheckedChangeListener((buttonView, isChecked) -> ConfigValue.CLEAR_FILTERS.setValue(isChecked));
        v.findViewById(R.id.cache_clear).setOnClickListener(v1 -> Executors.newSingleThreadExecutor().execute(() -> Glide.get(requireContext()).clearDiskCache()));


        //listener for buttons
        about = v.findViewById(R.id.aboutPage);
        about.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.nav_to_about));

        help = v.findViewById(R.id.helpPage);
        help.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.nav_to_help));

        //updates
        updates = v.findViewById(R.id.update_btn);
        updates.setOnClickListener(view -> {
            UpdateManager updateManager = new UpdateManager(requireContext());
            updateManager.checkForUpdates(true);
        });
       return v;
    }
}

