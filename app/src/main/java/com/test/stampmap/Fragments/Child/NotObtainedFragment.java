package com.test.stampmap.Fragments.Child;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.Adapter.noStampsRecViewAdapter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;


public class NotObtainedFragment extends Fragment {

    private RecyclerView noStampsView;
    private final ArrayList<Stamp> stamps = new ArrayList<>();
    noStampsRecViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_not_obtained, container, false);

        noStampsView = v.findViewById(R.id.noStampRecView);

        setData();

        noStampsRecyclerView();

        StampCollection.getInstance().addMyStampsUpdateEvent(this::setData);

        return v;
    }

    void setData(){
        stamps.clear();
        for (StampSet stampSet : StampCollection.getInstance().getAllStamps()){
            for (Stamp stamp : stampSet) {
                if (!stamp.getIsObtained() && stamp.getIsObtainable()) {
                    stamps.add(stamp);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void noStampsRecyclerView(){
        adapter = new noStampsRecViewAdapter(this.getContext(), stamps);
        noStampsView.setAdapter(adapter);

        noStampsView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
    }

}
