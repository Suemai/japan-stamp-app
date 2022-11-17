package com.test.stampmap.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.test.stampmap.Adapter.MyStampsViewPageAdapter;
import com.test.stampmap.R;
import org.jetbrains.annotations.NotNull;


public class MyStampsFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyStampsViewPageAdapter myStampsViewPageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_stamps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.myStampsTab);
        viewPager2 = view.findViewById(R.id.myStampsPageViewer);
        myStampsViewPageAdapter = new MyStampsViewPageAdapter(getActivity());
        viewPager2.setAdapter(myStampsViewPageAdapter);
    }
}