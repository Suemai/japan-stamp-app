package com.test.stampmap.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.test.stampmap.Activity.MainActivity;
import com.test.stampmap.Adapter.MyStampsViewPageAdapter;
import com.test.stampmap.Fragments.Child.NotObtainedFragment;
import com.test.stampmap.Fragments.Child.ObtainedFragment;
import com.test.stampmap.Fragments.Child.WishlistFragment;
import com.test.stampmap.R;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyStampsFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyStampsViewPageAdapter myStampsViewPageAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.my_stamps_fragment, container, false);

        view.findViewById(R.id.my_stamps_layout).setPadding(view.getPaddingLeft(), MainActivity.paddedStatusBarHeight, view.getPaddingRight(), view.getPaddingBottom());

        tabLayout = view.findViewById(R.id.myStampsTab);
        viewPager2 = view.findViewById(R.id.myStampsPageViewer);
        List<Fragment> myStampFragments = Arrays.stream(new Fragment[]{
                new ObtainedFragment(), new NotObtainedFragment(), new WishlistFragment()}).collect(Collectors.toList());
        myStampsViewPageAdapter = new MyStampsViewPageAdapter(requireActivity(), myStampFragments);
        viewPager2.setAdapter(myStampsViewPageAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            public void onTabUnselected(TabLayout.Tab tab) {}
            public void onTabReselected(TabLayout.Tab tab) {}
        });


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        myStampsViewPageAdapter.fragments.get(tabLayout.getSelectedTabPosition()).onResume();
    }
}