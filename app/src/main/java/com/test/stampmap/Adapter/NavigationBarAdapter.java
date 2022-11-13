package com.test.stampmap.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.test.stampmap.Fragments.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NavigationBarAdapter extends FragmentStateAdapter {
    List<Fragment> fragments;
    public NavigationBarAdapter(FragmentManager manager, Lifecycle lifecycle, List<Fragment> fragments) {
        super(manager, lifecycle);
        this.fragments = fragments;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
