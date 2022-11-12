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

public class MyStampsViewPageAdapter extends FragmentStateAdapter {
    public MyStampsViewPageAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ObtainedFragment();

            case 1:
                return new NotObtainedFragment();

            case 2:
                return new WishlistFragment();

            default:
                return new ObtainedFragment();
        }
    }

    @Override
    public int getItemCount() {
        //returns the number of tabs
        return 3;
    }
}
