package com.test.stampmap.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.test.stampmap.Adapter.MyStampsViewPageAdapter;
import com.test.stampmap.R;

public class MyStampsFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyStampsViewPageAdapter myStampsViewPageAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.my_stamps_fragment, container, false);

        tabLayout = view.findViewById(R.id.myStampsTab);
        viewPager2 = view.findViewById(R.id.myStampsPageViewer);
        myStampsViewPageAdapter = new MyStampsViewPageAdapter(requireActivity());
        viewPager2.setAdapter(myStampsViewPageAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
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
}