package com.test.stampmap.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.test.stampmap.Fragments.NotObtainedFragment;
import com.test.stampmap.Fragments.ObtainedFragment;
import com.test.stampmap.Fragments.WishlistFragment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyStampsViewPageAdapter extends FragmentStateAdapter {
    public List<Fragment> fragments;

    public MyStampsViewPageAdapter(FragmentActivity activity, List<Fragment> fragments) {
        super(activity);
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
