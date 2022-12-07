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
    private ArrayList<StampSet> stamps = new ArrayList<>();

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

        for (StampSet stampSet : StampCollection.getInstance().getAllStamps()){
            for (Stamp stamp : stampSet) {
                if (!stamp.getIsObtained() && stamp.getIsObtainable()) {
                    stamps.add(stampSet);
                }
            }
        }


        noStampsRecyclerView();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void noStampsRecyclerView(){
        noStampsRecViewAdapter adapter = new noStampsRecViewAdapter(this.getContext(), stamps);
        noStampsView.setAdapter(adapter);

        noStampsView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
    }

}
