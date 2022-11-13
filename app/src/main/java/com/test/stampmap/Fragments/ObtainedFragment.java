package com.test.stampmap.Fragments;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;

import java.util.ArrayList;
import java.util.HashMap;


public class ObtainedFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_obtained, container, false);

        ListView stampList = v.findViewById(R.id.stamp_list);
        ArrayList<HashMap<String, String>> dataSet = new ArrayList<>();
        String[] keys = {"num", "obtain", "location", "Owned"};
        int[] ids = {R.id.stampNo_drawer, R.id.obtainable_drawer, R.id.location_drawer, R.id.owned_drawer};
        for (StampSet stampSet : StampCollection.getInstance().getMyStamps()){
            HashMap<String, String> map = new HashMap<>();
            map.put(keys[0], stampSet.getName());
            map.put(keys[1], "Address: " + stampSet.getAddress());
            map.put(keys[2], "Difficulty: " + stampSet.getDifficulty());
            map.put(keys[3], "Open Hours: " + stampSet.getOpenHours());
            dataSet.add(map);
        }
        stampList.setAdapter(new SimpleAdapter(requireContext(), dataSet, R.layout.stamp_element, keys, ids));
        return v;
    }
}