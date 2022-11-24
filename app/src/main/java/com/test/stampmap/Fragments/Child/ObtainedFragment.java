package com.test.stampmap.Fragments.Child;

import android.os.Bundle;
import android.util.Log;
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

import java.text.DateFormat;
import java.util.*;


public class ObtainedFragment extends Fragment {
    ListView stampList;
    SimpleAdapter adapter;
    String[] keys = {"num", "obtain", "location", "Owned"};
    int[] ids = {R.id.stampNo_drawer, R.id.obtainable_drawer, R.id.location_drawer, R.id.owned_drawer};
    ArrayList<HashMap<String, String>> dataSet = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_obtained, container, false);
        stampList = v.findViewById(R.id.stamp_list);
        adapter = new SimpleAdapter(requireContext(), dataSet, R.layout.stamp_element, keys, ids);
        stampList.setAdapter(adapter);
        setData();
        StampCollection.getInstance().addMyStampsUpdateEvent(this::setData);
        return v;
    }

    void setData(){
        dataSet.clear();
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, Locale.JAPAN);
        for (StampSet stampSet : StampCollection.getInstance().getMyStamps()) {
            for (Stamp stamp : stampSet) {
                if (!stamp.getIsObtained()) continue;
                HashMap<String, String> map = new HashMap<>();
                map.put(keys[0], stampSet.getName());
                map.put(keys[1], "Address: " + stampSet.getAddress());
                map.put(keys[2], "Difficulty: " + stampSet.getDifficulty());
                map.put(keys[3], "Date Obtained: " + formatter.format(stamp.getDateObtained()));
                dataSet.add(map);
            }
        }
        dataSet.sort((o1, o2) -> Objects.requireNonNull(o1.get(keys[3])).compareTo(Objects.requireNonNull(o2.get(keys[3]))));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}