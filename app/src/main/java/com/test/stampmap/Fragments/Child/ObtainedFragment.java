package com.test.stampmap.Fragments.Child;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.Adapter.noStampsRecViewAdapter;
import com.test.stampmap.Adapter.obtainedRecViewAdapter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;

import java.text.DateFormat;
import java.util.*;


public class ObtainedFragment extends Fragment {
    private RecyclerView obtainedView;
    private final ArrayList<Stamp> stamps = new ArrayList<>();
    obtainedRecViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_obtained, container, false);

        obtainedView = v.findViewById(R.id.obtainedStampRecView);

        setData();

        obtainedRecyclerView();

        StampCollection.getInstance().addMyStampsUpdateEvent(this::setData);

        return v;
    }

    void setData(){
        stamps.clear();
        for (StampSet stampSet : StampCollection.getInstance().getMyStamps()) {
            for (Stamp stamp : stampSet) {
                if (!stamp.getIsObtained()) continue;
                stamps.add(stamp);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void obtainedRecyclerView(){

        adapter = new obtainedRecViewAdapter(this.getContext(), stamps);
        obtainedView.setAdapter(adapter);

        obtainedView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
    }
}