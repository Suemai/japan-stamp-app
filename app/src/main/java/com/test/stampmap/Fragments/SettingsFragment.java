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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.test.stampmap.Activity.MainActivity;
import com.test.stampmap.Fragments.SettingsChild.AboutFragment;
import com.test.stampmap.R;
import com.test.stampmap.Settings.ConfigValue;

import java.util.concurrent.Executors;


public class SettingsFragment extends Fragment {

    Fragment aboutFrag, helpFrag;
    FragmentManager childFragmentManager;
    FrameLayout settingsFrag;
    LinearLayout settingsLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.settings_fragment, container, false);

        //Initiazing the fragments
        aboutFrag = new AboutFragment();


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
//        TextView help = v.findViewById(R.id.helpPage);
//        help.setOnClickListener(view -> switchFragment(helpFrag));


        TextView about = v.findViewById(R.id.aboutPage);
        about.setOnClickListener(view -> switchFragment(aboutFrag));


        //testing cus why not
//        TextView about = v.findViewById(R.id.aboutPage);
//        about.setOnClickListener(view ->
//                Toast.makeText(about.getContext(), "Cyke! Not even implememted yet!", Toast.LENGTH_SHORT).show());

        TextView help = v.findViewById(R.id.helpPage);
        help.setOnClickListener(view ->
                Toast.makeText(help.getContext(), "Cyke! Not even implememted yet!", Toast.LENGTH_SHORT).show());


       return v;
    }

    private void switchFragment(Fragment fragment){
        childFragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = childFragmentManager.beginTransaction();

        transaction.replace(R.id.settings_frag_container, fragment)
                .addToBackStack(null)
                .commit();

//        // Hide the parent fragment's view
//        settingsLayout.setVisibility(View.GONE);
    }

}

