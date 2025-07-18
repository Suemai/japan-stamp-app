package com.test.stampmap.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.test.stampmap.Activity.MainActivity;
import com.test.stampmap.Adapter.LanguageSelectAdapter;
import com.test.stampmap.R;
import com.test.stampmap.Settings.ConfigValue;
import com.test.stampmap.Settings.SupportedLocale;
import com.test.stampmap.Settings.UpdateManager;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class SettingsFragment extends Fragment {

    FrameLayout settingsFrag;
    LinearLayout settingsLayout;

    TextView about, help, updates;
    View v;
    ViewGroup c;
    boolean loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("oh no", "creating settings fragment");
        v =  inflater.inflate(R.layout.settings_fragment, container, false);
        c = container;
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

        Spinner spinner = v.findViewById(R.id.language_select);
        List<SupportedLocale> locales = Arrays.stream(SupportedLocale.values().clone()).collect(Collectors.toList());
        LanguageSelectAdapter adapter = new LanguageSelectAdapter(requireContext(), android.R.layout.simple_spinner_item, locales);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        loading = true;
        spinner.setSelection(SupportedLocale.getCurrent().ordinal());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (loading) loading = false;
                else MainActivity.setLocale(requireActivity(), adapter.locales.get(position));
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
       return v;
    }
    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("bum", "THIS GETS CALLED");
        FrameLayout current = settingsFrag;
        v = onCreateView((LayoutInflater)requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), c, null);
        settingsFrag.removeAllViews();
        settingsFrag = current;
        settingsFrag.removeAllViews();
        settingsFrag.addView(settingsLayout);
    }
}

