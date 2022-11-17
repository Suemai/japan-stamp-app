package com.test.stampmap.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainViewPageAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public MainViewPageAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    };

    @Nullable
    public CharSequence getPageTitle(int position){
        return fragmentTitle.get(position);
    };
}
